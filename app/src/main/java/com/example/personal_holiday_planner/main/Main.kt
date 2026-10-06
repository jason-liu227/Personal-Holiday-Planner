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
    val budget = readlnOrNull()?.trim()?.toDoubleOrNull() ?: 0.0    #
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