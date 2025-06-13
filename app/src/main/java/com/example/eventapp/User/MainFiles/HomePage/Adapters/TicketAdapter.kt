package com.example.eventapp.User.MainFiles.HomePage.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.DataModels.TicketData
import com.example.eventapp.R
import java.text.SimpleDateFormat
import java.util.*

class TicketAdapter(
    private val context: Context,
    private val listener: TicketActionListener,
    private val ticketList: MutableList<TicketData>
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    interface TicketActionListener {
        fun onCancelClicked(ticket: TicketData)
        fun onSubmitFeedback(ticket: TicketData, feedback: String, rating: Int)
    }

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val ticketNumber: TextView = itemView.findViewById(R.id.ticketNumber)
        val date: TextView = itemView.findViewById(R.id.tv_date_label)
        val time: TextView = itemView.findViewById(R.id.tv_time_label)
        val venue: TextView = itemView.findViewById(R.id.role)
        val city: TextView = itemView.findViewById(R.id.email)
        val cancelBtn: AppCompatButton = itemView.findViewById(R.id.cancel_button)
        val feedbackBtn: AppCompatButton = itemView.findViewById(R.id.submit)
        val feedbackBox: EditText = itemView.findViewById(R.id.feedbackBox)
        val ratingText: TextView = itemView.findViewById(R.id.ratingText)
        val ratingLayout: View = itemView.findViewById(R.id.rating)
        val ratingLayout2:View = itemView.findViewById(R.id.ratingLayout)
        val stars: List<ImageView> = listOf(
            itemView.findViewById(R.id.start1),
            itemView.findViewById(R.id.start2),
            itemView.findViewById(R.id.start3),
            itemView.findViewById(R.id.start4),
            itemView.findViewById(R.id.start5)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_history_item, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]
        var selectedRating = 0

        holder.eventName.text = ticket.event_info.event_name
        holder.ticketNumber.text = "Number of tickets: ${ticket.noOfTicketsBought}"

        val (formattedDate, formattedTime) = formatDateTime(ticket.event_info.event_date)
        holder.date.text = formattedDate
        holder.time.text = formattedTime
        holder.venue.text = ticket.event_info.event_location.venue
        holder.city.text = ticket.event_info.event_location.city

        // Disable feedback input if already submitted
        holder.feedbackBtn.isEnabled = !ticket.feedbackSubmitted
        if (ticket.feedbackSubmitted == true) {
                holder.feedbackBox.visibility = View.GONE
                holder.feedbackBtn.setBackgroundResource(R.drawable.button_disabled)
                holder.feedbackBtn.text = "Feedback Submitted"
                holder.feedbackBtn.isEnabled = false
//                holder.ratingLayout.alpha = 0f
//                holder.ratingLayout.isClickable = false
//                holder.ratingLayout.isFocusable = false
            }
            else {
                holder.feedbackBox.visibility = View.VISIBLE
                holder.feedbackBox.isEnabled = true
                holder.feedbackBox.setText("") // clear input
                holder.feedbackBtn.text = "Submit Feedback"
                holder.feedbackBtn.isEnabled = true
//                holder.ratingLayout.alpha = 1f
//                holder.ratingLayout.isClickable = true
//                holder.ratingLayout.isFocusable = true
            }

        holder.feedbackBtn.text = if (ticket.feedbackSubmitted) "Feedback Submitted" else "Submit"

        // Cancel booking logic
        holder.cancelBtn.setOnClickListener {
            val updatedTicket = ticket.copy(isCancelled = true)
            ticketList[position] = updatedTicket
            notifyItemChanged(position)
            listener.onCancelClicked(ticket)
        }

        // Rating stars logic
        fun updateStars(rating: Int) {
            for (i in holder.stars.indices) {
                holder.stars[i].setImageResource(
                    if (i < rating) R.drawable.star else R.drawable.unwanted_star
                )
            }
        }
        val savedRating = ticket.rating ?: 5
        selectedRating = savedRating
        updateStars(savedRating)


        holder.stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (!ticket.feedbackSubmitted) {
                    selectedRating = index + 1
                    updateStars(selectedRating)
                }
            }
        }

        // Submit feedback logic
        holder.feedbackBtn.setOnClickListener {
            val feedback = holder.feedbackBox.text.toString().trim()

            if (feedback.isNotEmpty() && selectedRating > 0) {
                listener.onSubmitFeedback(ticket, feedback, selectedRating)
                ticket.feedbackSubmitted = true
                notifyItemChanged(position)
            } else {
                Toast.makeText(
                    context,
                    "Please enter feedback and select rating before submitting.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Cancel button status
        holder.cancelBtn.isEnabled = !ticket.isCancelled
        holder.cancelBtn.text = if (ticket.isCancelled) "Cancelled" else "Cancel Booking"
        holder.cancelBtn.setTextColor(
            if (ticket.isCancelled)
                ContextCompat.getColor(context, android.R.color.holo_red_dark)
            else
                ContextCompat.getColor(context, R.color.dark_olive_green)
        )

        // Feedback visibility if cancelled
        val visibility = if (ticket.isCancelled) View.GONE else View.VISIBLE
        holder.ratingText.visibility = visibility
        holder.ratingLayout.visibility = visibility
        holder.feedbackBox.visibility = visibility
        holder.feedbackBtn.visibility = visibility
        holder.itemView.alpha = if (ticket.isCancelled) 0.6f else 1f
    }


    override fun getItemCount(): Int = ticketList.size

    fun updateList(newList: List<TicketData>) {
        ticketList.clear()
        ticketList.addAll(newList)
        notifyDataSetChanged()
    }

    private fun formatDateTime(isoDate: String): Pair<String, String> {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(isoDate)
            val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            Pair(dateFormat.format(date ?: Date()), timeFormat.format(date ?: Date()))
        } catch (e: Exception) {
            Pair("Unknown Date", "Unknown Time")
        }
    }
}
