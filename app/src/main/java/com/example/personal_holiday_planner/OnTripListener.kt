package com.example.personal_holiday_planner

import com.example.personal_holiday_planner.models.TripModel

interface OnTripListener {

    fun onTripClick(trip: TripModel)

    fun onTripLongClick(trip: TripModel): Boolean
}