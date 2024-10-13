package com.example.submissioneventdicoding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.model.Event

class EventAdapter(private val events: List<Event>, private val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val eventImage: ImageView = itemView.findViewById(R.id.event_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.name
        Glide.with(holder.itemView.context).load(event.imageLogo).into(holder.eventImage)

        holder.itemView.setOnClickListener { onClick(event) }
    }

    override fun getItemCount(): Int = events.size
}
