package com.example.personal_holiday_planner

import android.content.Context
import com.example.personal_holiday_planner.stores.TripMemStore
import com.example.personal_holiday_planner.stores.TripStore
import java.io.File
import timber.log.Timber

object AppData {
    lateinit var bookedTrips: TripStore
        private set

    fun initialize(context: Context) {
        if (::bookedTrips.isInitialized) return
        val file = File(
            context.filesDir,
            "trip_list.json"
        )

        bookedTrips = TripMemStore(file)

        Timber.d("AppData initialized")
    }
}