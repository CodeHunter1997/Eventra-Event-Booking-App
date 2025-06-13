package com.example.eventapp.DataModels

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class EventListResponse(
    val status: Int,
    val message: String,
    val data: List<EventMinimal>
)

data class EventMinimal(
    val _id: String,
    val event_name: String,
    val description: String,
    val date: String,          // Format: "2025-05-23T00:00:00.000Z"
    val time: String,          // Format: "10:00"
    val location: Location,
    val type: String,          // "offline" or "online"
    val online_link: String?,   // Nullable for offline events
    val total_seats: Int,
    val booked_seats: Int,
    val available_seats: Int,
    val ticketPrice: Int,
    val category: String,      // e.g., "Dance"
    val status: String,        // e.g., "upcoming"
    val organizerId: String,
    val organizerName: String,
    val artistname: String,
    val artistrole: String,
    val validAge: Boolean,
    val images: List<String>,  // e.g., ["eminem.jpeg-1747169390038.jpeg"]
    val videos: List<String>?, // Nullable list of videos
    val isDeleted: Boolean,
    val createdAt: String,     // Format: "2025-05-13T20:49:50.264Z"
    val updatedAt: String      // Format: "2025-05-16T13:07:04.793Z"
) {
    // Helper property to get first image URL
    val mainImageUrl: String?
        get() = images.firstOrNull()
}

data class Location(
    val venue: String,
    val city: String
)

//data class StaticEvent(val name: String, val date: String)
data class ApiResponse<T>(
    @SerializedName("data")
    val data: T,
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("message")
    val message: String? = null
)
data class UpcomingEvent(
    val _id: String,
    val event_name: String,
    val description: String,
    val date: String,
    val time: String,
    val location: EventLocation,
    val type: String,
    val online_link: String?,
    val total_seats: Int,
    val booked_seats: Int,
    val available_seats: Int,
    val ticketPrice: Int,
    val category: String,
    val status: String,
    val organizerId: String,
    val organizerName: String,
    val artistname: String,
    val artistrole: String,
    val validAge: Boolean,
    val images: List<String>,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    data class EventLocation(
        val venue: String,
        val city: String
    )
    val mainImageUrl: String?
        get() = images.firstOrNull()

}

