package com.example.personal_holiday_planner.stores

import com.example.personal_holiday_planner.models.TripModel
import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber

class TripMemStore : TripStore {

    private val trips = ArrayList<TripModel>()
    private val lastId = AtomicLong(0L)

    override fun findAll(): List<TripModel> {
        Timber.d("Retrieving all trips: ${trips.size}")
        return trips
    }

    override fun findOne(id: Long): TripModel? {
        Timber.d("Finding trip id=$id")
        return trips.find { t -> t.id == id }
    }

    override fun create(trip: TripModel) {
        trip.id = lastId.incrementAndGet()
        trips.add(trip)
        Timber.d("Created trip id=${trip.id}")
    }

    override fun update(trip: TripModel): Boolean {
        val foundTrip = findOne(trip.id)

        return if (foundTrip != null) {
            foundTrip.destination = trip.destination
            foundTrip.startDate = trip.startDate
            foundTrip.endDate = trip.endDate
            foundTrip.budget = trip.budget

            Timber.d("Updated trip id=${trip.id}")
            true
        } else {
            Timber.w("Unable to update trip id=${trip.id}")
            false
        }
    }

    override fun delete(id: Long): Boolean {
        val foundTrip = findOne(id)

        return if (foundTrip != null) {
            trips.remove(foundTrip)
            Timber.d("Deleted trip id=$id")
            true
        } else {
            Timber.w("Unable to delete trip id=$id")
            false
        }
    }
}