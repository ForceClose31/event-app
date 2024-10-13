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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.model.Event

class EventDetailFragment : Fragment() {

    private lateinit var event: Event
    private lateinit var progressBar: ProgressBar

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
        progressBar = view.findViewById(R.id.progress_bar)

        loadEventDetails(eventImage, eventName, eventOwner, eventTime, eventQuota, eventDescription)

        btnLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(browserIntent)
        }

        return view
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
        val remainingQuota = event.quota - event.registrant
        eventQuota.text = "Sisa Kuota: $remainingQuota"
        eventDescription.text = event.description

        progressBar.visibility = View.GONE
    }
}