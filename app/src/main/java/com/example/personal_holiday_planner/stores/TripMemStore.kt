package com.example.personal_holiday_planner.stores

import com.example.personal_holiday_planner.models.TripModel

class TripMemStore {

    private val trips = ArrayList<TripModel>()

    fun findAll(): List<TripModel> {
        return trips
    }

}