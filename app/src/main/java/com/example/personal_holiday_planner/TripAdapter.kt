package com.example.personal_holiday_planner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_holiday_planner.models.TripModel

class TripAdapter(
    private var trips: List<TripModel>,
    private val onEdit: (TripModel) -> Unit,
    private val onDelete: (TripModel) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

                val destinationText: TextView =
                    itemView.findViewById(R.id.destinationText)

                val budgetText: TextView =
                    itemView.findViewById(R.id.budgetText)

                val dateText: TextView =
                    itemView.findViewById(R.id.dateText)

                val editButton: Button =
                    itemView.findViewById(R.id.editButton)

                val deleteButton: Button =
                    itemView.findViewById(R.id.deleteButton)
            }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TripViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trips, parent, false)

        return TripViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TripViewHolder,
        position: Int
    ) {
        val trip = trips[position]

        holder.destinationText.text =
            "${trip.id}: ${trip.destination}"

        holder.budgetText.text =
            "Budget: ${trip.budget}"

        holder.dateText.text =
            "${trip.startDate} - ${trip.endDate}"

        holder.editButton.setOnClickListener {
            onEdit(trip)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(trip)
        }
    }

    override fun getItemCount(): Int{
        return trips.size
    }

    fun updateTrips(newTrips: List<TripModel>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}