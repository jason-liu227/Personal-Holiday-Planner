package com.example.personal_holiday_planner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_holiday_planner.models.TripModel
import timber.log.Timber

class TripListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)

        recyclerView = findViewById(R.id.tripsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TripAdapter(
            trips = AppData.bookedtrips.findAll(),

            listener = object : OnTripListener {

                override fun onTripClick(trip: TripModel) {
                    editTrip(trip)
                }

                override fun onTripLongClick(
                    trip: TripModel
                ): Boolean {

                    confirmDelete(trip)

                    return true
                }
            }
        )

        recyclerView.adapter = adapter

        val returnButton =
            findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {
            finish()
        }

        Timber.d("TripListActivity opened")
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            adapter.updateTrips(
                AppData.bookedtrips.findAll()
            )
        }
    }

    private fun editTrip(trip: TripModel) {

        Timber.d("Editing trip id=${trip.id}")

        val intent = Intent(
            this, AddEditActivity::class.java
        )

        intent.putExtra("id", trip.id)

        startActivity(intent)
    }

    private fun confirmDelete(trip: TripModel) {

        AlertDialog.Builder(this)
            .setTitle("Delete Trip")
            .setMessage(
                "Are you sure you want to delete " +
                        "this holiday trip?"
            )
            .setPositiveButton("Delete") { _, _ ->

                deleteTrip(trip)
            }
            .setNegativeButton("Cancel") { dialog, _ ->

                Timber.d("Trip deletion cancelled")

                dialog.dismiss()
            }
            .show()
    }

    private fun deleteTrip(trip: TripModel) {

        AppData.bookedtrips.delete(trip.id)

        adapter.updateTrips(
            AppData.bookedtrips.findAll()
        )

        Timber.d("Deleted trip id=${trip.id}")
    }
}