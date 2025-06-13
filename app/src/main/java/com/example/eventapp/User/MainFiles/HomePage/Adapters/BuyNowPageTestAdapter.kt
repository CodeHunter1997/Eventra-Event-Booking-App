package com.example.eventapp.User.MainFiles.HomePage.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.DataModels.TestimonialDataSuccessfulResponse
import com.example.eventapp.DataModels.UpcomingEvents.TestimonialData
import com.example.eventapp.R

    class BuyNowPageTestAdapter(private val testimonials: List<TestimonialDataSuccessfulResponse>) :
        RecyclerView.Adapter<BuyNowPageTestAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val testimonialText: TextView = itemView.findViewById(R.id.commentText)
            val stars = listOf<ImageView>(
                itemView.findViewById(R.id.star1),
                itemView.findViewById(R.id.star2),
                itemView.findViewById(R.id.star3),
                itemView.findViewById(R.id.star4),
                itemView.findViewById(R.id.star5)
            )
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_booking_testimonial, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = testimonials.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = testimonials[position]
            holder.testimonialText.text = data.commentdata

            // Set stars based on rating
            for (i in 0 until 5) {
                if (i < data.rating) {
                    holder.stars[i].setImageResource(R.drawable.star)
                } else {
                    holder.stars[i].setImageResource(R.drawable.unwanted_star)
                }
            }
        }
    }
