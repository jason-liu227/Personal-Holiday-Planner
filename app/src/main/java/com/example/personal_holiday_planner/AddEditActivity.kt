package com.example.personal_holiday_planner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_holiday_planner.models.TripModel
import timber.log.Timber
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.content.Intent

class AddEditActivity : AppCompatActivity() {

    private lateinit var destinationInput: EditText
    private lateinit var budgetInput: EditText
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText
    private lateinit var imagePreview: ImageView
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private var selectedImageUri: Uri? = null
    private var editingId: Long? = null
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null

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
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)
        findViewById<Button>(R.id.imagePickButton)
            .setOnClickListener {
                imagePicker.launch(arrayOf("image/*"))
            }

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        val mapPicker =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->

                if (result.resultCode == RESULT_OK) {

                    val data = result.data
                        ?: return@registerForActivityResult

                    selectedLatitude =
                        data.getDoubleExtra("latitude", 0.0)

                    selectedLongitude =
                        data.getDoubleExtra("longitude", 0.0)

                    selectedLatitude?.let {
                        latitudeInput.setText(it.toString())
                    }

                    selectedLongitude?.let {
                        longitudeInput.setText(it.toString())
                    }

                    Timber.d(
                        "Location selected: $selectedLatitude, $selectedLongitude"
                    )
                }
            }

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
        selectedImageUri = trip.imageUri?.let {
            Uri.parse(it)
        }
        selectedLatitude = trip.latitude
        selectedLongitude = trip.longitude

        latitudeInput.setText(trip.latitude.toString())
        longitudeInput.setText(trip.longitude.toString())

        Timber.d("Location restored for trip id=${trip.id}")

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(
                android.R.drawable.ic_menu_gallery
            )
        }

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

        val latitude =
            latitudeInput.text.toString().trim().toDoubleOrNull()

        val longitude =
            longitudeInput.text.toString().trim().toDoubleOrNull()

        if (destination.isEmpty()) {

            Timber.w("Validation failed: Empty destination")
            destinationInput.error = "Destination is required"
            return
        }

        if (budget == null || !budget.isFinite() || budget < 0) {
            Timber.w("Validation failed: Invalid budget")
            budgetInput.error = "Enter a valid budget"
            return
        }

        if (startDate.isEmpty()) {
            startDateInput.error = "Start date is required"
            return
        }

        if (endDate.isEmpty()) {
            endDateInput.error = "End date is required"
            return
        }

        if (latitude == null) {
            latitudeInput.error = "Select a valid latitude"
            return
        }

        if (longitude == null) {
            longitudeInput.error = "Select a valid longitude"
            return
        }

        try {
            if (editingId == null || editingId == -1L) {

                val trip = TripModel(
                    destination = destination,
                    budget = budget,
                    startDate = startDate,
                    endDate = endDate,
                    imageUri = selectedImageUri?.toString(),
                    latitude = latitude,
                    longitude = longitude
                )

                AppData.bookedTrips.create(trip)

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
                    endDate = endDate,
                    imageUri = selectedImageUri?.toString(),
                    latitude = latitude,
                    longitude = longitude
                )

                val updated = AppData.bookedTrips.update(trip)
                if (!updated) {
                    Toast.makeText(
                        this,
                        "Unable to update trip",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                Timber.d("Existing trip updated: id=${trip.id}")
                Toast.makeText(
                    this,
                    "Trip updated",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()

        } catch (e: IllegalArgumentException) {
            Timber.w("Validation failed: ${e.message}")
            Toast.makeText(
                this,
                e.message ?: "Please check the entered values.",
                Toast.LENGTH_LONG
            ).show()
        }
        finish()
    }
}