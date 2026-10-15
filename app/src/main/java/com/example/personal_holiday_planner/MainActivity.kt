package com.example.personal_holiday_planner

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate

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

        val darkMode = getSharedPreferences(
            "planner_settings", MODE_PRIVATE
        ).getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        val settingsButton = findViewById<Button>(R.id.settingsTab)

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

        settingsButton.setOnClickListener {
            val currentMode = AppCompatDelegate.getDefaultNightMode()

            val isDark = when (currentMode) {
                AppCompatDelegate.MODE_NIGHT_YES -> true
                AppCompatDelegate.MODE_NIGHT_NO -> false
                else -> (resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_YES
            }

            val options = arrayOf("Light Mode", "Dark Mode")

            AlertDialog.Builder(this)
                .setTitle("Settings")
                .setSingleChoiceItems(
                    options,
                    if (isDark) 1 else 0
                ) { dialog, which ->

                    getSharedPreferences(
                        "planner_settings", MODE_PRIVATE
                    ).edit()
                        .putBoolean("dark_mode", which == 1)
                        .apply()

                    AppCompatDelegate.setDefaultNightMode(
                        if (which == 1)
                            AppCompatDelegate.MODE_NIGHT_YES
                        else
                            AppCompatDelegate.MODE_NIGHT_NO
                    )
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}