package com.example.eventapp.Organizer.Authentication.MainFiles.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.DataModels.EventData2
import com.example.eventapp.DataModels.OrganizerEventListResponse
import com.example.eventapp.R

class MyEventAdapter(
    eventList: List<EventData2>,
    private val onItemClick: (EventData2) -> Unit
) : RecyclerView.Adapter<MyEventAdapter.EventViewHolder>() {

    private val eventList = eventList.toMutableList()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventCategory: TextView = itemView.findViewById(R.id.eventCategory)
        val eventStatus: TextView = itemView.findViewById(R.id.eventStatus)

        init {
            itemView.setOnClickListener {
                val event = eventList[adapterPosition]
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_status_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventName.text = event.event_name
        holder.eventCategory.text = event.category

        if (event.isDeleted) {
            holder.eventStatus.text = "Cancelled"
            holder.eventStatus.setTextColor(holder.itemView.context.getColor(R.color.gray))
            holder.eventStatus.background = null
        } else {
            holder.eventStatus.text = "Active"
            holder.eventStatus.setTextColor(holder.itemView.context.getColor(R.color.green))
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateList(newEvents: List<EventData2>) {
        eventList.clear()
        eventList.addAll(newEvents)
        notifyDataSetChanged()
    }


}
