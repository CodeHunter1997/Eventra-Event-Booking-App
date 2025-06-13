//package com.example.eventapp.User.MainFiles.HomePage.Adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.eventapp.DataModels.StaticEvent
//import com.example.eventapp.R
//
//class EventPagerAdapter(
//    private val onItemClick: (StaticEvent) -> Unit
//) : RecyclerView.Adapter<EventPagerAdapter.EventViewHolder>() {
//
//    private val events = listOf(
//        StaticEvent("Tech Expo 2025", "10.06.2025"),
//        StaticEvent("Art & Culture Fest", "15.06.2025"),
//        StaticEvent("Startup Meetup", "01.07.2025")
//    )
//
//    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val eventName: TextView = view.findViewById(R.id.textEventName)
//        private val eventDate: TextView = view.findViewById(R.id.textEventDate)
//        private val bookNowButton: Button = view.findViewById(R.id.buyTickets)
//
//        fun bind(event: StaticEvent) {
//            eventName.text = event.name
//            eventDate.text = event.date
//
//
//
//            bookNowButton.setOnClickListener {
//                onItemClick(event)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.user_item_view_pager, parent, false)
//        return EventViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        holder.bind(events[position])
//    }
//
//    override fun getItemCount(): Int = events.size
//}