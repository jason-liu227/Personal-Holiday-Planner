package com.example.personal_holiday_planner

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainTitle)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewTripsButton = findViewById<Button>(R.id.viewTripsButton)

        val addTripButton = findViewById<Button>(R.id.returnButton)

        val viewTab = findViewById<Button>(R.id.viewTab)

        val addTab = findViewById<Button>(R.id.addTab)

        viewTripsButton.setOnClickListener {
            startActivity(
                Intent(this, TripListActivity::class.java)
            )
        }

        addTripButton.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }

        viewTab.setOnClickListener {
            startActivity(
                Intent(this, TripListActivity::class.java)
            )
        }

        addTab.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }



    }
}