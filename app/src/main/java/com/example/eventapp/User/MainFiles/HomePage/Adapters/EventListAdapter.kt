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
import com.example.eventapp.DataModels.EventMinimal
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.BuyNowActivity
import java.text.SimpleDateFormat
import java.util.Locale

//for event fragment cards
class EventListAdapter(
        private var events: List<EventMinimal>,
    // The onItemClick lambda is now handling the redirection
    // private val onItemClick: ((EventMinimal) -> Unit)? = null
) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private var originalEvents = events.toMutableList()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameTextView: TextView = itemView.findViewById(R.id.eventType)
        val categoryTextView: TextView = itemView.findViewById(R.id.category)
        val cityTextView: TextView = itemView.findViewById(R.id.email)
        val priceTextView: TextView = itemView.findViewById(R.id.ticketPrice)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date_label)
        val eventImageView: ImageView = itemView.findViewById(R.id.eventImage)
//        val bgImage: ImageView = itemView.findViewById(R.id.bgImage)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedEvent = events[pos]
                    // Create an Intent to navigate to Test_Activity
                    val intent = Intent(itemView.context, BuyNowActivity::class.java)

                    // Pass the event data using Intent extras
                    intent.putExtra("_id", clickedEvent._id)
                    intent.putExtra("category", clickedEvent.category)
                    intent.putExtra("event_name", clickedEvent.event_name)
                    intent.putExtra("ticketPrice", clickedEvent.ticketPrice.toString()) // Convert to String
                    intent.putExtra("description", clickedEvent.description)
                    intent.putExtra("date", clickedEvent.date)
                    intent.putExtra("time", clickedEvent.time)
                    intent.putExtra("venue", clickedEvent.location.venue)
                    intent.putExtra("city", clickedEvent.location.city)
                    intent.putExtra("image_url", clickedEvent.mainImageUrl) // Pass the image URL


                    // Start the Test_Activity
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item_layout, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.eventNameTextView.text = event.event_name
        holder.categoryTextView.text = event.category
        holder.cityTextView.text = event.location.city
        holder.priceTextView.text = "₹${event.ticketPrice}"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = try {
            inputFormat.parse(event.date)?.let { outputFormat.format(it) } ?: event.date
        } catch (e: Exception) {
            event.date
        }
        holder.dateTextView.text = formattedDate

        val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${event.mainImageUrl}"
        Log.d("GlideImageURL", fullImageUrl)
        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .placeholder(R.drawable.error_image)
            .error(R.drawable.sample_details_page)
            .into(holder.eventImageView)
}

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<EventMinimal>) {
        events = newEvents
        originalEvents = newEvents.toMutableList()
        notifyDataSetChanged()
    }

    fun filterEvents(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.ROOT)
        events = originalEvents.filter { event ->
            event.event_name.lowercase(Locale.ROOT).contains(lowerCaseQuery) ||
                    event.category.lowercase(Locale.ROOT).contains(lowerCaseQuery) ||
                    event.location.city.lowercase(Locale.ROOT).contains(lowerCaseQuery)
        }
        notifyDataSetChanged()
    }
}