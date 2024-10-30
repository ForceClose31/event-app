package com.example.submissioneventdicoding.ui.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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

class CompletedEventsFragment : Fragment() {

    private lateinit var completedEventAdapter: EventAdapter
    private lateinit var rvCompletedEvents: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_completed_events, container, false)

        progressBar = view.findViewById(R.id.progress_completed)
        rvCompletedEvents = view.findViewById(R.id.rv_completed_events)
        rvCompletedEvents.layoutManager = LinearLayoutManager(context)

        searchView = view.findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchEvents(query, status = 0)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        loadCompletedEvents()

        return view
    }

    private fun loadCompletedEvents() {
        showLoading(true)
        RetrofitClient.instance.getCompletedEvents(0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val completedEvents = response.body()?.listEvents ?: emptyList()
                    Log.d("CompletedEvents", "Events fetched: $completedEvents")
                    completedEventAdapter = EventAdapter(completedEvents) { event ->
                        val eventDetailFragment = EventDetailFragment()
                        val bundle = Bundle()
                        bundle.putParcelable("event", event)
                        eventDetailFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_container, eventDetailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    rvCompletedEvents.adapter = completedEventAdapter
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun searchEvents(query: String, status: Int) {
        RetrofitClient.instance.searchEvents(keyword = query, active = status).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val searchResults = response.body()?.listEvents ?: emptyList()
                    completedEventAdapter = EventAdapter(searchResults) { event ->
                        val eventDetailFragment = EventDetailFragment()
                        val bundle = Bundle()
                        bundle.putParcelable("event", event)
                        eventDetailFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_container, eventDetailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    rvCompletedEvents.adapter = completedEventAdapter
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
