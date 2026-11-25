package com.example.personal_holiday_planner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_holiday_planner.models.TripModel
import timber.log.Timber
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri


class AddEditActivity : AppCompatActivity() {

    private lateinit var destinationInput: EditText
    private lateinit var budgetInput: EditText
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText
    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null
    private var editingId: Long? = null

     val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            if (uri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Timber.w("Unable to retain image permission")
                }
                selectedImageUri = uri
                imagePreview.setImageURI(uri)
                Timber.d("Holiday image selected")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        Timber.d("AddEditActivity opened")
        destinationInput = findViewById(R.id.destinationInput)
        budgetInput = findViewById(R.id.budgetInput)
        startDateInput = findViewById(R.id.startDateInput)
        endDateInput = findViewById(R.id.endDateInput)
        imagePreview = findViewById(R.id.imageViewAdd)
        findViewById<Button>(R.id.imagePickButton)
            .setOnClickListener {
                imagePicker.launch(arrayOf("image/*"))
            }

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener {
            Timber.d("Save Trip button clicked")
            saveTrip()
        }

        cancelButton.setOnClickListener {
            Timber.d("Trip form cancelled")
            finish()
        }

        editingId = intent.getLongExtra("id", -1L)
        if (editingId != -1L) {
            Timber.d("Editing trip id=$editingId")
            loadExistingTrip(editingId!!)
        } else {
            Timber.d("Creating a new holiday trip")
        }
    }

    private fun loadExistingTrip(id: Long) {

        Timber.d("Loading existing trip id=$id")

        val trip = AppData.bookedTrips.findOne(id)
        if (trip == null) {
            Timber.w("Trip not found: id=$id")
            Toast.makeText(
                this,
                "Trip not found",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        destinationInput.setText(trip.destination)
        budgetInput.setText(trip.budget.toString())
        startDateInput.setText(trip.startDate)
        endDateInput.setText(trip.endDate)

        Timber.d("Trip loaded successfully: id=$id")
    }

    private fun saveTrip() {

        val destination =
            destinationInput.text.toString().trim()

        val budget =
            budgetInput.text.toString().trim().toDoubleOrNull()

        val startDate =
            startDateInput.text.toString().trim()

        val endDate =
            endDateInput.text.toString().trim()

        if (destination.isEmpty()) {
            Timber.w("Validation failed: Destination is empty")
            destinationInput.error = "Destination is required"
            return
        }

        if (budget == null || budget < 0) {
            Timber.w("Validation failed: Invalid budget")
            budgetInput.error = "Enter a valid budget"
            return
        }

        if (startDate.isEmpty()) {
            Timber.w("Validation failed: Start date is empty")
            startDateInput.error = "Start date is required"
            return
        }

        if (endDate.isEmpty()) {
            Timber.w("Validation failed: End date is empty")
            endDateInput.error = "End date is required"
            return
        }

        if (editingId == null || editingId == -1L) {

            val trip = TripModel(
                destination = destination,
                budget = budget,
                startDate = startDate,
                endDate = endDate
            )

            AppData.bookedTrips.create(trip)

            // Timber logging
            Timber.d("New trip created: id=${trip.id}")

            Toast.makeText(
                this,
                "Trip created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val trip = TripModel(
                id = editingId!!,
                destination = destination,
                budget = budget,
                startDate = startDate,
                endDate = endDate
            )

            AppData.bookedTrips.update(trip)

            // Timber logging
            Timber.d("Existing trip updated: id=${trip.id}")

            Toast.makeText(
                this,
                "Trip updated",
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }
}