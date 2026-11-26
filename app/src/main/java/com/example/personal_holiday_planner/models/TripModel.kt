package com.example.personal_holiday_planner.models

import java.text.SimpleDateFormat
import java.util.Locale

data class TripModel(

    var id: Long = 0L,
    var destination: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var budget: Double = 0.0,
    var imageUri: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {

    fun validate() {

        require(destination.isNotBlank()) {
            "Destination must not be empty."
        }

        require(budget.isFinite() && budget >= 0) {
            "Budget must be zero or greater."
        }

        require(isValidDate(startDate)) {
            "Enter a valid start date (YYYY-MM-DD)."
        }

        require(isValidDate(endDate)) {
            "Enter a valid end date (YYYY-MM-DD)."
        }

        require(endDate >= startDate) {
            "End date cannot be before start date."
        }

        require(latitude.isFinite() && latitude in -90.0..90.0) {
            "Latitude must be between -90 and 90."
        }

        require(longitude.isFinite() && longitude in -180.0..180.0) {
            "Longitude must be between -180 and 180."
        }
    }

    private fun isValidDate(date: String): Boolean {

        val formatter = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        )

        formatter.isLenient = false

        return try {

            val parsedDate = formatter.parse(date)

            parsedDate != null &&
                    formatter.format(parsedDate) == date

        } catch (e: java.text.ParseException) {

            false
        }
    }
}