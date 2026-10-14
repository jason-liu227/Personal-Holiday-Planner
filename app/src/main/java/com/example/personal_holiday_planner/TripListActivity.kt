package com.example.personal_holiday_planner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_holiday_planner.models.TripModel

class TripListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)

        recyclerView =
            findViewById(R.id.tripsRecyclerView)

        adapter = TripAdapter(
            trips = AppData.bookedtrips.findAll(),
            onEdit = { trip ->
                editTrip(trip)
            },

            onDelete = { trip ->
                deleteTrip(trip)
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        val returnButton =
            findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            adapter.updateTrips(
                AppData.bookedtrips.findAll()
            )
        }
    }

    private fun editTrip(trip: TripModel ) {
        val intent =
            Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", trip.id)
        startActivity(intent)
    }

    private fun deleteTrip(trip: TripModel ) {
        AppData.bookedtrips.delete(trip.id)
        adapter.updateTrips(
            AppData.bookedtrips.findAll()
        )
    }
}