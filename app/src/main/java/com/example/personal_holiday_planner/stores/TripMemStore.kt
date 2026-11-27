package com.example.personal_holiday_planner.stores

import com.example.personal_holiday_planner.models.TripModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber
import java.io.File

class TripMemStore(private val file: File) : TripStore {
    private val trips = ArrayList<TripModel>()
    private val lastId = AtomicLong(0L)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }

    override fun findAll(): List<TripModel> {
        Timber.d("Retrieving all trips: ${trips.size}")
        return trips
    }

    override fun findOne(id: Long): TripModel? {
        Timber.d("Finding trip id=$id")
        return trips.find { t -> t.id == id }
    }

    override fun create(trip: TripModel) {
        trip.validate()
        trip.id = lastId.incrementAndGet()
        trips.add(trip)
        save()
        Timber.d("Created trip id=${trip.id}")
    }

    override fun update(trip: TripModel): Boolean {
        trip.validate()

        val foundTrip = findOne(trip.id)
        return if (foundTrip != null) {
            foundTrip.destination = trip.destination
            foundTrip.startDate = trip.startDate
            foundTrip.endDate = trip.endDate
            foundTrip.budget = trip.budget
            foundTrip.imageUri = trip.imageUri
            foundTrip.latitude = trip.latitude
            foundTrip.longitude = trip.longitude

            save()

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
            save()
            Timber.d("Deleted trip id=$id")
            true
        } else {
            Timber.w("Unable to delete trip id=$id")
            false
        }
    }

    private fun load() {

        if (!file.exists()) {
            Timber.d("No existing JSON file found")
            return
        }

        val type =
            object : TypeToken<ArrayList<TripModel>>() {}.type

        val loaded: ArrayList<TripModel> =
            gson.fromJson(file.readText(), type)
                ?: arrayListOf()

        trips.clear()
        trips.addAll(loaded)

        lastId.set(trips.maxOfOrNull { it.id } ?: 0L)

        Timber.d("Loaded ${trips.size} trips from JSON")
    }

    private fun save() {
        file.parentFile?.mkdirs()
        file.writeText(gson.toJson(trips))

        Timber.d("Saved ${trips.size} trips to JSON")
    }
}