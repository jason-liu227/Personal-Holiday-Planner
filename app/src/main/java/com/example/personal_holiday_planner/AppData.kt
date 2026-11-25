package com.example.personal_holiday_planner

import com.example.personal_holiday_planner.stores.TripMemStore
import com.example.personal_holiday_planner.stores.TripStore

object AppData {
    val bookedTrips: TripStore = TripMemStore()
}