package com.example.personal_holiday_planner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_holiday_planner.models.TripModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var destinationInput: EditText
    private lateinit var budgetInput: EditText
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()

        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            loadExistingTrip(editingId!!)
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        destinationInput = EditText(this).apply {
            hint = "Destination"
        }

        budgetInput = EditText(this).apply {
            hint = "Budget"
            inputType =
                android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        startDateInput = EditText(this).apply {
            hint = "Start Date (e.g. 2026-10-20)"
        }

        endDateInput = EditText(this).apply {
            hint = "End Date (e.g. 2026-10-27)"
        }

        val saveButton = Button(this).apply {
            text = "Save Trip"
            setOnClickListener {
                saveTrip()
            }
        }

        val cancelButton = Button(this).apply {
            text = "Cancel"
            setOnClickListener {
                finish()
            }
        }

        root.addView(destinationInput)
        root.addView(budgetInput)
        root.addView(startDateInput)
        root.addView(endDateInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
    }

    private fun loadExistingTrip(id: Long) {

        val trip = AppData.bookedtrips.findOne(id)

        if (trip == null) {
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
    }

    private fun saveTrip() {

        val destination = destinationInput.text.toString().trim()
        val budget = budgetInput.text.toString().trim().toDoubleOrNull()
        val startDate = startDateInput.text.toString().trim()
        val endDate = endDateInput.text.toString().trim()

        if (destination.isEmpty()) {
            destinationInput.error = "Destination is required"
            return
        }

        if (budget == null || budget < 0) {
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


        if (editingId == null || editingId == -1L) {

            val trip = TripModel(
                destination = destination,
                budget = budget,
                startDate = startDate,
                endDate = endDate
            )

            AppData.bookedtrips.create(trip)

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

            AppData.bookedtrips.update(trip)

            Toast.makeText(
                this,
                "Trip updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}