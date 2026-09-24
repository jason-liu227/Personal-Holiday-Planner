package com.example.personal_holiday_planner.stores
import com.example.personal_holiday_planner.models.TripModel

interface TripStore {
    fun findAll(): List<TripModel>
    fun create(trip: TripModel)
    fun update(trip: TripModel): Boolean
    fun delete(id: Long): Boolean
    fun findOne(id: Long): TripModel?
}