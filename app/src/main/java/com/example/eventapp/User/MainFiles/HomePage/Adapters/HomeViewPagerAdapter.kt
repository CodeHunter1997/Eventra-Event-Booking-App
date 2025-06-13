package com.example.eventapp.User.MainFiles.HomePage.Adapters

import android.content.Context
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

class HomeViewPagerAdapter(private val events: List<EventMinimal>, private val context: Context) :
    RecyclerView.Adapter<HomeViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPagerItem)
        val txt_1: TextView = itemView.findViewById(R.id.txt_1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewpager_image, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.txt_1.text = events[position].event_name.toString()

        val event = events[position]
        val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${event.mainImageUrl}"
        Log.d("GlideImageURL", fullImageUrl)
        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .placeholder(R.drawable.error_image)
            .error(R.drawable.sample_details_page)
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            val intent = Intent(context, BuyNowActivity::class.java).apply {
                putExtra("_id", events[position]._id.toString())
                putExtra("category", events[position].category.toString())
                putExtra("event_name", events[position].event_name.toString())
                putExtra("ticketPrice", events[position].ticketPrice.toString())
                putExtra("description", events[position].description.toString())
                putExtra("date", events[position].date.toString())
                putExtra("time", events[position].time.toString())
                putExtra("image_url", events[position].mainImageUrl.toString())
                // Handle venue and city separately
                putExtra("venue", events[position].location.venue.toString())
                putExtra("city", events[position].location.city.toString())
                putExtra("artist_name", events[position].artistname.toString())
                putExtra("artist_role", events[position].artistrole.toString())
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = events.size
}