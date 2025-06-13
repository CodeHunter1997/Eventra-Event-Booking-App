package com.example.eventapp.User.MainFiles.HomePage.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.DataModels.EventMinimal
import com.example.eventapp.DataModels.UpcomingEvents
import com.example.eventapp.DataModels.UpcomingEvents.CategoryModel
import com.example.eventapp.R

class CategoryAdapter(
    private val categories: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.categoryName)
        // If you want an icon, you can add an ImageView here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = categories[position]
        holder.name.text = categoryName
        holder.itemView.setOnClickListener { onItemClick(categoryName) }
    }

    override fun getItemCount(): Int = categories.size
}
