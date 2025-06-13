package com.example.eventapp.OnboardingScreen

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eventapp.R
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.graphics.Color
import com.example.eventapp.ExtraFiles.UserTypeLogin

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageSlide)
        val title: TextView = itemView.findViewById(R.id.title)
        val getStarted: Button = itemView.findViewById(R.id.btnGetStarted)
        val login =itemView.findViewById<TextView>(R.id.loginTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.onboarding_slider, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = items[position]
        holder.image.setImageResource(item.imageResId)
        holder.title.text = item.title

        holder.getStarted.text = if (position == items.lastIndex) "Get Started" else "Next"

        holder.getStarted.setOnClickListener {
            if (position == items.lastIndex) {
                val intent = Intent(holder.itemView.context, UserTypeLogin::class.java)
                holder.itemView.context.startActivity(intent)
            } else {
                viewPager.currentItem = position + 1
            }
        }
        holder.getStarted.text = if (position == items.lastIndex) "Get Started" else "Next"
        holder.getStarted.visibility = View.VISIBLE

        holder.login.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserTypeLogin::class.java)
            holder.itemView.context.startActivity(intent)
        }

        //word color change method

        val rawTitle = when (position) {
            0 -> holder.itemView.context.getString(R.string.title_slide1) // "Feel the Beat, Live the Night"
            1 -> holder.itemView.context.getString(R.string.title_slide2) // "Seamless Planning, Impactful Results"
            2 -> holder.itemView.context.getString(R.string.title_slide3) // "Where Strategy Takes the Spotlight"
            else -> item.title
        }


        val spannableString = SpannableString(rawTitle)
        val highlightWord = when (position) {
            0 -> "Beat"
            1 -> "Planning"
            2 -> "Strategy"
            else -> ""
        }
        val color = Color.parseColor("#E0FF00")

        val startIndex = rawTitle.indexOf(highlightWord)
        val endIndex = startIndex + highlightWord.length

        if (startIndex >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                0
            )
        }
        holder.title.text = spannableString

    }

    override fun getItemCount(): Int = items.size
}
