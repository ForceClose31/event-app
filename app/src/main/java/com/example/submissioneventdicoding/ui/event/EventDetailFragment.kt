package com.example.submissioneventdicoding.ui.event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.model.Event
import com.example.submissioneventdicoding.database.AppDatabase
import com.example.submissioneventdicoding.database.FavoriteEventEntity
import com.example.submissioneventdicoding.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.text.Html
import android.text.method.LinkMovementMethod

class EventDetailFragment : Fragment() {

    private lateinit var event: Event
    private lateinit var progressBar: ProgressBar
    private lateinit var repository: EventRepository
    private lateinit var btnFavorite: Button
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_detail, container, false)
        event = arguments?.getParcelable("event") ?: Event("", "", "", "", 0, 0, "", "", "")
        val eventImage: ImageView = view.findViewById(R.id.event_image_detail)
        val eventName: TextView = view.findViewById(R.id.event_name_detail)
        val eventOwner: TextView = view.findViewById(R.id.event_owner)
        val eventTime: TextView = view.findViewById(R.id.event_time)
        val eventQuota: TextView = view.findViewById(R.id.event_quota)
        val eventDescription: TextView = view.findViewById(R.id.event_description)
        val btnLink: Button = view.findViewById(R.id.btn_open_link)
        btnFavorite = view.findViewById(R.id.btn_favorite)
        progressBar = view.findViewById(R.id.progress_bar)

        val db = AppDatabase.getDatabase(requireContext())
        repository = EventRepository(db)

        loadEventDetails(eventImage, eventName, eventOwner, eventTime, eventQuota, eventDescription)

        checkFavoriteStatus()

        btnLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(browserIntent)
        }

        btnFavorite.setOnClickListener {
            if (isFavorite) {
                removeFromFavorites()
            } else {
                addToFavorites()
            }
        }

        return view
    }

    private fun checkFavoriteStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            val favorites = repository.getAllFavorites()
            isFavorite = favorites.any { it.id == event.id }
            withContext(Dispatchers.Main) {
                updateFavoriteButton()
            }
        }
    }

    private fun addToFavorites() {
        val favoriteEvent = FavoriteEventEntity(event.id, event.name, event.beginTime, event.description, event.imageLogo)
        CoroutineScope(Dispatchers.IO).launch {
            repository.addFavorite(favoriteEvent)
            isFavorite = true
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                updateFavoriteButton()
            }
        }
    }

    private fun removeFromFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeFavorite(event.id)
            isFavorite = false
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                updateFavoriteButton()
            }
        }
    }

    private fun updateFavoriteButton() {
        btnFavorite.text = if (isFavorite) {
            "Hapus dari Favorite"
        } else {
            "Tambah ke Favorite"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadEventDetails(
        eventImage: ImageView,
        eventName: TextView,
        eventOwner: TextView,
        eventTime: TextView,
        eventQuota: TextView,
        eventDescription: TextView
    ) {
        progressBar.visibility = View.VISIBLE

        Glide.with(requireContext())
            .load(event.imageLogo)
            .into(eventImage)

        eventName.text = event.name
        eventOwner.text = "Penyelenggara: ${event.ownerName}"
        eventTime.text = "Waktu: ${event.beginTime}"
        val remainingQuota = event.quota - event.registrants
        eventQuota.text = "Sisa Kuota: $remainingQuota"
        val descriptionHtml = event.description
        eventDescription.text = Html.fromHtml(descriptionHtml, Html.FROM_HTML_MODE_LEGACY)
        eventDescription.movementMethod = LinkMovementMethod.getInstance()

        progressBar.visibility = View.GONE
    }
}
