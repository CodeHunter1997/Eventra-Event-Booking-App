package com.example.eventapp.User.MainFiles.HomePage.Adapters

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
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.BuyNowActivity
import java.text.SimpleDateFormat
import java.util.Locale

class UpcomingEventsPageAdapter(private var upcomingEvents: List<UpcomingEvent>) :
    RecyclerView.Adapter<UpcomingEventsPageAdapter.UpcomingEventViewHolder>() {

    inner class UpcomingEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameTextView: TextView = itemView.findViewById(R.id.eventType)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date_label)
        val cityTextView: TextView = itemView.findViewById(R.id.email)
        val ticketPriceTextView: TextView = itemView.findViewById(R.id.ticketPrice)
        val categoryTextView: TextView = itemView.findViewById(R.id.category)
        val eventImageView: ImageView = itemView.findViewById(R.id.eventImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_event2, parent, false)
        return UpcomingEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        val event = upcomingEvents[position]
        holder.eventNameTextView.text = event.event_name

        // Format and set date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = try {
            inputFormat.parse(event.date)?.let { outputFormat.format(it) } ?: event.date
        } catch (e: Exception) {
            event.date
        }
        holder.dateTextView.text = formattedDate

        // Set city, ticket price, and category
        holder.cityTextView.text = event.location.city
        holder.ticketPriceTextView.text = "₹${event.ticketPrice}"
        holder.categoryTextView.text = event.category ?: ""

        // Load image (first image from list or placeholder)
        val imageUrl = event.images.firstOrNull()
        Glide.with(holder.itemView.context)
            .load(imageUrl ?: R.drawable.event_card_img)
            .placeholder(R.drawable.event_card_img)
            .error(R.drawable.event_card_img)
            .into(holder.eventImageView)

        // Click to open detail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BuyNowActivity::class.java).apply {
                putExtra("_id", event._id)
                putExtra("category", event.category ?: "")
                putExtra("event_name", event.event_name ?: "")
                putExtra("ticketPrice", event.ticketPrice.toString())
                putExtra("description", event.description ?: "")
                putExtra("date", event.date ?: "")
                putExtra("time", event.time ?: "")
                putExtra("venue", event.location.venue ?: "")
                putExtra("city", event.location.city ?: "")
            }
            context.startActivity(intent)
        }

        val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${event.mainImageUrl}"
        Log.d("GlideImageURL", fullImageUrl)
        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .placeholder(R.drawable.error_image)
            .error(R.drawable.sample_details_page)
            .into(holder.eventImageView)
    }

    override fun getItemCount(): Int = upcomingEvents.size

    fun updateData(newEvents: List<UpcomingEvent>) {
        upcomingEvents = newEvents
        notifyDataSetChanged()
    }
}
