package com.example.submissioneventdicoding.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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

class HomeFragment : Fragment() {

    private lateinit var activeEventAdapter: EventAdapter
    private lateinit var completedEventAdapter: EventAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var rvActiveEvents: RecyclerView
    private lateinit var rvCompletedEvents: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        progressBar = view.findViewById(R.id.progress)
        rvActiveEvents = view.findViewById(R.id.rv_active_events)
        rvCompletedEvents = view.findViewById(R.id.rv_completed_events)

        rvActiveEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCompletedEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        loadEventData()

        return view
    }

    private fun loadEventData() {
        showLoading(true)

        // Mengambil Event Aktif
        RetrofitClient.instance.getActiveEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val activeEvents = response.body()?.listEvents ?: emptyList()
                    Log.d("HomeFragment", "Active Events: $activeEvents")
                    activeEventAdapter = EventAdapter(activeEvents) { event ->
                        // Handle event click here
                    }
                    rvActiveEvents.adapter = activeEventAdapter
                } else {
                    Log.e("HomeFragment", "Error fetching active events: ${response.code()}")
                    showErrorToast()
                }
                showLoading(false) // Pindahkan ke sini untuk menampilkan loading hanya setelah respons diterima
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e("HomeFragment", "Failure fetching active events", t)
                showErrorToast()
            }
        })

        // Mengambil Event Selesai
        RetrofitClient.instance.getCompletedEvents(0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val completedEvents = response.body()?.listEvents ?: emptyList()
                    Log.d("HomeFragment", "Completed Events: $completedEvents")
                    completedEventAdapter = EventAdapter(completedEvents) { event ->
                        // Handle event click here
                    }
                    rvCompletedEvents.adapter = completedEventAdapter
                } else {
                    Log.e("HomeFragment", "Error fetching completed events: ${response.code()}")
                    showErrorToast()
                }
                showLoading(false) // Pindahkan ke sini untuk menampilkan loading hanya setelah respons diterima
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e("HomeFragment", "Failure fetching completed events", t)
                showErrorToast()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
    }
}
