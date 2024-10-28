package com.example.submissioneventdicoding.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.adapter.FavoriteEventAdapter
import com.example.submissioneventdicoding.database.AppDatabase
import com.example.submissioneventdicoding.database.FavoriteEventEntity
import com.example.submissioneventdicoding.ui.event.EventDetailFragment
import com.example.submissioneventdicoding.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteEventsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteEventAdapter: FavoriteEventAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorite_events, container, false)
        recyclerView = view.findViewById(R.id.rv_favorite_events)
        recyclerView.layoutManager = LinearLayoutManager(context)

        db = AppDatabase.getDatabase(requireContext())
        loadFavorites()

        return view
    }

    private fun loadFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            val favorites = db.favoriteEventDao().getAllFavorites()
            withContext(Dispatchers.Main) {
                favoriteEventAdapter = FavoriteEventAdapter(favorites) { event ->
                    val eventDetailFragment = EventDetailFragment()

                    val bundle = Bundle().apply {
                        putParcelable("event", event.toEvent())
                    }
                    eventDetailFragment.arguments = bundle

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, eventDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
                recyclerView.adapter = favoriteEventAdapter
            }
        }
    }
}

fun FavoriteEventEntity.toEvent(): Event {
    return Event(
        id = this.id,
        name = this.name,
        ownerName = "", 
        beginTime = this.date,
        quota = this.quota,
        registrants = this.registrants,
        imageLogo = this.imageLogo,
        description = this.description,
        link = "" 
    )
}