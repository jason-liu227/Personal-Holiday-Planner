package com.example.personal_holiday_planner.stores

import com.example.personal_holiday_planner.models.TripModel
import java.util.concurrent.atomic.AtomicLong

class TripMemStore {

    private val trips = ArrayList<TripModel>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<TripModel> {
        return trips
    }

    fun findOne(id: Long): TripModel? {
        return trips.find { t -> t.id ==id }
    }

    fun create(trip: TripModel) {
        trip.id = lastId.incrementAndGet()
        trips.add(trip)
    }

    fun update(trip: TripModel): Boolean {
        val foundTrip = findOne(trip.id)
        return if (foundTrip != null) {
            foundTrip.destination = trip.destination
            foundTrip.startDate = trip.startDate
            foundTrip.endDate = trip.endDate
            foundTrip.budget = trip.budget#
            true
        }
        else{
            false
        }
    }


}