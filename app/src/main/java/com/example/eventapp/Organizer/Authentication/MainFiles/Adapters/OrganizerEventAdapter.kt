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
import com.example.eventapp.DataModels.EventMinimal
import com.example.eventapp.Organizer.Authentication.MainFiles.OrganizerDetailsActivity
import com.example.eventapp.R
import java.text.SimpleDateFormat
import java.util.Locale

//for home page cards
class OrganizerEventAdapter(
    private var events: List<EventMinimal>,
    // The onItemClick lambda is now handling the redirection
    // private val onItemClick: ((EventMinimal) -> Unit)? = null
) : RecyclerView.Adapter<OrganizerEventAdapter.EventViewHolder>() {

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.eventType)
        val eventDate: TextView = view.findViewById(R.id.tv_date_label)
        val eventCity: TextView = view.findViewById(R.id.email)
        val ticketPrice: TextView = view.findViewById(R.id.ticketPrice)
        val perPerson: TextView = view.findViewById(R.id.perPerson)
        val imageView: ImageView = view.findViewById(R.id.image)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val clickedEvent = events[pos]
                    // Create an Intent to navigate to Test_Activity
                    val intent = Intent(itemView.context, OrganizerDetailsActivity::class.java)

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
                    intent.putExtra("type", clickedEvent.type)
                    intent.putExtra("status", clickedEvent.status)
                    intent.putExtra("image_url", clickedEvent.mainImageUrl)

                    // Start the Test_Activity
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.org_home_card_adapter, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        with(holder) {
            eventName.text = event.event_name

            // Date formatting
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = try {
                val dateObj = inputFormat.parse(event.date)
                if (dateObj != null) {
                    outputFormat.format(dateObj)
                } else {
                    event.date // Or a default value
                }
            } catch (e: Exception) {
                e.printStackTrace()
                event.date // Or a default value
            }
            eventDate.text = formattedDate

            eventCity.text = event.location.city
            ticketPrice.text = "₹${event.ticketPrice}"
            perPerson.text = "/Person"

            if (!event.mainImageUrl.isNullOrEmpty()) {
                val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${event.mainImageUrl}"
                Log.d("GlideImageURL", "Loaded URL: $fullImageUrl")

                Glide.with(holder.itemView.context)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.sample_details_page)
                    .error(R.drawable.sample_details_page)
                    .into(imageView)
            } else {
                Log.d("GlideImageURL", "Image URL is empty or null for event: ${event.event_name}")
                imageView.setImageResource(R.drawable.sample_details_page) // fallback image
            }

        }
    }

    fun updateEvents(newEvents: List<EventMinimal>) {
        events = newEvents
        notifyDataSetChanged()
    }
}