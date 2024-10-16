package com.example.submissioneventdicoding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.database.FavoriteEventEntity

class FavoriteEventAdapter(private val events: List<FavoriteEventEntity>,  private val onClick: (FavoriteEventEntity) -> Unit) :
    RecyclerView.Adapter<FavoriteEventAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.tv_event_name)
        val eventDate: TextView = view.findViewById(R.id.tv_event_date)
        val eventImage: ImageView = view.findViewById(R.id.event_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.name
        holder.eventDate.text = event.date
        Glide.with(holder.itemView.context)
            .load(event.imageLogo)
            .into(holder.eventImage)

        holder.itemView.setOnClickListener { onClick(event) }
    }

    override fun getItemCount(): Int = events.size
}
