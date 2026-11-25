package com.example.personal_holiday_planner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_holiday_planner.models.TripModel
import timber.log.Timber

class TripAdapter(
    private var trips: List<TripModel>,
    private val listener: OnTripListener
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val destinationText: TextView =
            itemView.findViewById(R.id.destinationText)

        val budgetText: TextView =
            itemView.findViewById(R.id.budgetText)

        val dateText: TextView =
            itemView.findViewById(R.id.dateText)
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

        holder.destinationText.text = trip.destination

        holder.budgetText.text = "Budget: €${trip.budget}"

        holder.dateText.text =
            "${trip.startDate} - ${trip.endDate}"

        holder.itemView.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                val selectedTrip = trips[currentPosition]

                Timber.d("Trip clicked: id=${selectedTrip.id}")

                listener.onTripClick(selectedTrip)
            }
        }

        holder.itemView.setOnLongClickListener {
            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                val selectedTrip = trips[currentPosition]

                Timber.d("Trip long clicked: id=${selectedTrip.id}")

                listener.onTripLongClick(selectedTrip)
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    fun updateTrips(newTrips: List<TripModel>) {
        trips = newTrips
        notifyDataSetChanged()

        Timber.d("Trip list refreshed: ${trips.size} trips")
    }
}