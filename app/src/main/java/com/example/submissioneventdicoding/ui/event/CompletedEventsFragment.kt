package com.example.submissioneventdicoding.ui.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.adapter.EventAdapter
import com.example.submissioneventdicoding.api.RetrofitClient
import com.example.submissioneventdicoding.model.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletedEventsFragment : Fragment() {

    private lateinit var completedEventAdapter: EventAdapter
    private lateinit var rvCompletedEvents: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_completed_events, container, false)

        rvCompletedEvents = view.findViewById(R.id.rv_completed_events)
        rvCompletedEvents.layoutManager = LinearLayoutManager(context)

        loadCompletedEvents()

        return view
    }

    private fun loadCompletedEvents() {
        val activeStatus = 0
        val url = "https://event-api.dicoding.dev/events?active=$activeStatus"
        Log.d("API Request", "Fetching events from URL: $url")
        RetrofitClient.instance.getCompletedEvents(activeStatus).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val completedEvents = response.body()?.listEvents ?: emptyList()
                    Log.d("CompletedEvents", "Events fetched: $completedEvents")
                    completedEventAdapter = EventAdapter(completedEvents) { event ->
                        // Handle item click
                    }
                    rvCompletedEvents.adapter = completedEventAdapter
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
