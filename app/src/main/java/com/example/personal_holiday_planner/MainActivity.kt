package com.example.personal_holiday_planner

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()
    }

    override fun onResume() {
        super.onResume()

        if (::listLayout.isInitialized) {
            displayTrips()
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Holiday Planner"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val addButton = Button(this).apply {
            text = "Add Trip"
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                startActivity(intent)
            }
        }

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            addButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            listLayout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)

        displayTrips()
    }

    private fun displayTrips() {

        listLayout.removeAllViews()

        val trips = AppData.bookedtrips.findAll()

        if (trips.isEmpty()) {

            val emptyText = TextView(this).apply {
                text = "No trips yet. Plan your first Holiday!"
                textSize = 18f
                setPadding(0, 40, 0, 40)
            }

            listLayout.addView(emptyText)

            return
        }

        for (trip in trips) {

            val tripLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }

            val tripTitle = TextView(this).apply {
                text = "${trip.id}: ${trip.destination}"
                textSize = 20f
            }

            val tripDates = TextView(this).apply {
                text = "${trip.startDate} - ${trip.endDate}"
                textSize = 16f
            }

            val tripBudget = TextView(this).apply {
                text = "Budget: ${trip.budget}"
                textSize = 14f
            }

            val editButton = Button(this).apply {
                text = "Edit"

                setOnClickListener {
                    val intent = Intent(
                        this@MainActivity,
                        AddEditActivity::class.java
                    )

                    intent.putExtra("id", trip.id)

                    startActivity(intent)
                }
            }

            val deleteButton = Button(this).apply {
                text = "Delete"

                setOnClickListener {
                    AppData.bookedtrips.delete(trip.id)
                    displayTrips()
                }
            }

            tripLayout.addView(tripTitle)
            tripLayout.addView(tripDates)
            tripLayout.addView(tripBudget)
            tripLayout.addView(editButton)
            tripLayout.addView(deleteButton)

            listLayout.addView(tripLayout)
        }
    }
}
