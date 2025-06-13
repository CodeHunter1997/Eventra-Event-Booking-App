package com.example.eventapp.User.MainFiles.HomePage.Adapters

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.eventapp.DataModels.EventMinimal
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.BuyNowActivity
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter(
    private var events: List<EventMinimal>,
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

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
                    val intent = Intent(itemView.context, BuyNowActivity::class.java).apply {
                        putExtra("_id", clickedEvent._id)
                        putExtra("category", clickedEvent.category)
                        putExtra("event_name", clickedEvent.event_name)
                        putExtra("ticketPrice", clickedEvent.ticketPrice.toString())
                        putExtra("description", clickedEvent.description)
                        putExtra("date", clickedEvent.date)
                        putExtra("time", clickedEvent.time)
                        putExtra("venue", clickedEvent.location.venue)
                        putExtra("city", clickedEvent.location.city)
                        putExtra("image_url", clickedEvent.mainImageUrl)
                    }
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

            // Format the date
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = try {
                inputFormat.parse(event.date)?.let { outputFormat.format(it) } ?: event.date
            } catch (e: Exception) {
                Log.e("DateParseError", "Error parsing date: ${event.date}", e)
                event.date
            }
            eventDate.text = formattedDate

            eventCity.text = event.location.city
            ticketPrice.text = "₹${event.ticketPrice}"
            perPerson.text = "/Person"

            // Load the first image directly if available
            val mainImageFilename = event.mainImageUrl
            if (!mainImageFilename.isNullOrEmpty()) {
                val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/$mainImageFilename"
                Log.d("GlideImageURL", "Loading image from: $fullImageUrl")

                Glide.with(imageView.context)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.error_image)
                    .error(R.drawable.sample_details_page)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                        ): Boolean {
                            Log.e("GLIDE_ERROR", "Failed to load image: $fullImageUrl", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(imageView)
            } else {
                // Fallback if image list is empty
                imageView.setImageResource(R.drawable.sample_details_page)
                Log.w("ImageLoad", "No image available for event: ${event.event_name}")
            }
        }
    }

    fun updateEvents(newEvents: List<EventMinimal>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
