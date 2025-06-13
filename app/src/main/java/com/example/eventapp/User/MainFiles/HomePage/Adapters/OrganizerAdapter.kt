package com.example.eventapp.User.MainFiles.HomePage.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventapp.DataModels.Organizer
import com.example.eventapp.DataModels.Organizer2
import com.example.eventapp.R

class OrganizerAdapter(private val organizerList: List<Organizer2>) :
    RecyclerView.Adapter<OrganizerAdapter.OrganizerViewHolder>() {

    class OrganizerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.organizerName)
        val imageView: ImageView = itemView.findViewById(R.id.organizerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_organizer, parent, false)
        return OrganizerViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganizerViewHolder, position: Int) {
        val organizer = organizerList[position]

        // It shows first + last name if available, otherwise show company name
        val displayName = if (!organizer.first_name.isNullOrBlank() && !organizer.last_name.isNullOrBlank()) {
            "${organizer.first_name} ${organizer.last_name}"
        } else {
            organizer.company_name ?: "Unknown Organizer"
        }
        holder.nameText.text = displayName

        val imageUrl = organizer.image?.let {
            "https://eventra-server.onrender.com/" + it.replace("\\", "/")
        }
        Log.d("OrganizerUrl", imageUrl.toString())


        if (imageUrl != null) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.org1)
                .into(holder.imageView)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.org1)
                .into(holder.imageView)
        }
    }


    override fun getItemCount(): Int = organizerList.size
}
