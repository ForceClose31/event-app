package com.example.submissioneventdicoding.ui.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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

class ActiveEventsFragment : Fragment() {

    private lateinit var activeEventAdapter: EventAdapter
    private lateinit var rvActiveEvents: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_active_events, container, false)

        rvActiveEvents = view.findViewById(R.id.rv_active_events)
        rvActiveEvents.layoutManager = LinearLayoutManager(context)

        searchView = view.findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchEvents(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        loadActiveEvents()

        return view
    }

    private fun loadActiveEvents() {
        RetrofitClient.instance.getActiveEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val activeEvents = response.body()?.listEvents ?: emptyList()
                    Log.d("CompletedEvents", "Events fetched: $activeEvents")
                    activeEventAdapter = EventAdapter(activeEvents) { event ->
                        val eventDetailFragment = EventDetailFragment()
                        val bundle = Bundle()
                        bundle.putParcelable("event", event)
                        eventDetailFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, eventDetailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    rvActiveEvents.adapter = activeEventAdapter
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun searchEvents(query: String) {
        RetrofitClient.instance.searchEvents(keyword = query).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val searchResults = response.body()?.listEvents ?: emptyList()
                    activeEventAdapter = EventAdapter(searchResults) { event ->
                        val eventDetailFragment = EventDetailFragment()
                        val bundle = Bundle()
                        bundle.putParcelable("event", event)
                        eventDetailFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, eventDetailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    rvActiveEvents.adapter = activeEventAdapter
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
