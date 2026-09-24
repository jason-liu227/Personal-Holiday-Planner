package com.example.personal_holiday_planner.stores

import com.example.personal_holiday_planner.models.TripModel
import java.util.concurrent.atomic.AtomicLong

class TripMemStore {

    private val trips = ArrayList<TripModel>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<TripModel> {
        return trips
    }

    fun create(trip: TripModel) {
        trip.id = lastId.incrementAndGet()
        trips.add(trip)
    }

}