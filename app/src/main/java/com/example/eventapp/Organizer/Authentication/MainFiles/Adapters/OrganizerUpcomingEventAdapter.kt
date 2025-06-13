package com.example.eventapp.Organizer.Authentication.MainFiles.Adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventapp.DataModels.UpcomingEvent
import com.example.eventapp.Organizer.Authentication.MainFiles.OrganizerDetailsActivity
import com.example.eventapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class OrganizerUpcomingEventAdapter(private var upcomingEvents: List<UpcomingEvent>) :
    RecyclerView.Adapter<OrganizerUpcomingEventAdapter.UpcomingEventViewHolder>() {

    inner class UpcomingEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameTextView: TextView = itemView.findViewById(R.id.eventType) // Replace with your IDs
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date_label)   // Replace with your IDs
        val cityTextView: TextView = itemView.findViewById(R.id.email)
        val eventImageView: ImageView = itemView.findViewById(R.id.image)
        val ticketPriceTextView: TextView = itemView.findViewById(R.id.ticketPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_event, parent, false) // Replace with your item layout
        return UpcomingEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        val event = upcomingEvents[position]
        holder.eventNameTextView.text = event.event_name

        // Format the date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = try {
            inputFormat.parse(event.date)?.let { outputFormat.format(it) } ?: event.date
        } catch (e: Exception) {
            event.date
        }
        holder.dateTextView.text = formattedDate
        holder.ticketPriceTextView.text = "₹${event.ticketPrice}"

        holder.cityTextView.text = event.location.city
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, OrganizerDetailsActivity::class.java).apply {
                putExtra("_id", event._id)
                putExtra("category", event.category ?: "")
                putExtra("event_name", event.event_name ?: "")
                putExtra("ticketPrice", event.ticketPrice.toString())
                putExtra("description", event.description ?: "")
                putExtra("date", event.date ?: "")
                putExtra("time", event.time ?: "")
                putExtra("status", event.status ?: "")
                putExtra("type", event.type ?: "")
                putExtra("venue", event.location.venue ?: "")
                putExtra("city", event.location.city ?: "")
                putExtra("image_url", event.mainImageUrl ?:"")
            }
            context.startActivity(intent)
        }
        val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${event.mainImageUrl}"
        Log.d("GlideImageURL", fullImageUrl)
        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .placeholder(R.drawable.sample_details_page)
            .error(R.drawable.sample_details_page)
            .into(holder.eventImageView)
    }

    override fun getItemCount(): Int = upcomingEvents.size

    fun updateData(newEvents: List<UpcomingEvent>) {
        upcomingEvents = newEvents
        notifyDataSetChanged()
    }
}