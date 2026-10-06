package com.example.personal_holiday_planner.main

import com.example.personal_holiday_planner.stores.TripMemStore
import com.example.personal_holiday_planner.models.TripModel

val store = TripMemStore()

fun main(){
    println("Holiday Planner Console App")
    var input: Int
    do {
        input = menu()
        when (input) {
            1 -> addTrip()
            2 -> listTrips()
            3 -> updateTrip()
            else -> println("\n Invalid option. Please try again.")
        }
    }
        while (input != 0)
}

fun menu(): Int {
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Trip")
    println(" 2. List All Trips")
    println(" 3. Update a Trip")
    println(" 4. Delete a Trip")
    println(" 5. Search Trip by ID")
    println(" 0. Exit")
    print("\nEnter option: ")
    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun addTrip() {
    println("\n--- Add Placemark ---")
    print("Enter Destination: ")
    val destination = readlnOrNull()?.trim().orEmpty()
    print("Enter Budget: ")
    val budget = readlnOrNull()?.trim()?.toDoubleOrNull() ?: 0.0
    print("Start Date: ")
    val startDate = readlnOrNull()?.trim().orEmpty()
    print("End Date")
    val endDate = readlnOrNull()?.trim().orEmpty()

    if (destination.isNotEmpty()) {
        val trip = TripModel(
            destination = destination,
            budget = budget,
            startDate = startDate,
            endDate = endDate
        )
        store.create(trip)
        println("Trip added successfully with ID: ${trip.id}")
    } else {
        println("Title cannot be empty. Unable to create trip")
    }
}

fun listTrips() {
    println("\n--- All Trips ---")
    val trips = store.findAll()
    if(trips.isEmpty()) {
        println("No trips made yet.")
    } else {
        trips.forEach { println("ID: ${it.id} | Destination: ${it.destination} | Budget: ${it.budget} | Start Date: ${it.startDate} | End Date: ${it.endDate}") }
    }
}

fun updateTrip() {
    println("\n--- Update Trip ---")
    listTrips()
    if (store.findAll().isEmpty()) return

    print("\n Enter ID of Trip to update: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null && store.findOne(id) != null) {
        print("Enter new Destination: ")
        val destination = readlnOrNull()?.trim().orEmpty()
        print("Enter new Budget: ")
        val budget = readlnOrNull()?.trim()?.toDoubleOrNull() ?: 0.0
        print("Enter new Start Date: ")
        val startDate = readlnOrNull()?.trim().orEmpty()
        print("Enter new End Date")
        val endDate = readlnOrNull()?.trim().orEmpty()

        if (destination.isNotEmpty()) {
            val updated = store.update(TripModel(id = id, destination = destination, budget = budget, startDate = startDate, endDate = endDate))
            if (updated) println("Trip updated successfully.")
        } else {
            println("Trip cannot be empty. Update cancelled.")
        }
    } else {
        println("Trip with ID $id not found.")
    }
}