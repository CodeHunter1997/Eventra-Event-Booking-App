package com.example.eventapp.DataModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File


data class OrganizerResponse(
    val message: String,
    val data: List<Organizer2>
)

data class Organizer2(
    val _id: String,
    val first_name: String,
    val last_name: String,
    val company_name: String,
    val email: String,
    val image: String
)


data class OrganizerListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Organizer>
)

data class Organizer(
    val company_name: String,
    val image: String,
    )
data class OrganizerEventListResponse(
    val message: String,
    val data: List<EventData2>
)

@Parcelize
data class EventData2(
    val _id: String,
    val event_name: String,
    val description: String,
    val date: String,
    val time: String,
    val location: Location,
    val type: String,
    val total_seats: Int,
    val booked_seats: Int,
    val available_seats: Int,
    val ticketPrice: Int,
    val category: String,
    val artistname: String,
    val artistrole: String,
    val status: String,
    val images: List<String>,
    val videos: List<String>,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
) : Parcelable {
    @Parcelize
    data class Location(
        val venue: String,
        val city: String
    ) : Parcelable
}
    data class OrganizerDashboardResponse(
    val message: String,
    val data: OrganizerData,
)


data class OrganizerData(
    val _id: String,
    val company_name: String,
    val image: String,
    val email: String,
    val phone: Long,
    val role: String,
    val isdelete: Boolean,
    val isThirteenPlus: Boolean,
    val isActive: Boolean,
    val isadmindelete: Boolean,
    val admin_msg: String,
    val isverify: Boolean,
    val createdAt: String,
    val updatedAt: String

)


data class OrganizerDashboardData(
    val totalRevenue: Int,       // or Double, depending on the API
    val totalTicketsSold: Int,
    val liveEventsCount: Int,
    val deletedEventsCount: Int,
    val completedEventsCount: Int
)
data class OrganizerEventListApiResponse(
    val message: String,
    val data: List<OrganizerEvent>
)

data class OrganizerEvent(
    val _id: String,
    val event_name: String,
    val description: String,
    val date: String,
    val time: String,
    val location: Location,
    val type: String,
    val online_link: String?, // nullable
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
    val videos: List<String>,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    data class Location(
        val venue: String,
        val city: String
    )
}

data class EventCreateRequest(
    val event_name: String,
    val description: String,
    val date: String,
    val time: String,
    val city: String,
    val venue: String,
    val total_seats: Int,
    val ticketPrice: Int,
    val type: String,
    val artistname: String,
    val artistrole: String,
    val category: String,
    val imageFile: File? = null // Optional now
)


data class CreateEventResponse(
    val status: Int,
    val message: String,
    val error: String)



data class UpdateEventRequest(
    val event_name: String,
    val description: String,
    val date: String,
    val time: String,
    val location: Location,
    val total_seats: Int,
    val ticketPrice: Int,
    val type: String,
    val artistname: String,
    val artistrole: String,
    val category: String,
    val validAge: Boolean
)
{
data class Location(
    val city: String,
    val venue: String
)
}
data class EventUpdateResponse(
    val status: Int,
    val message: String,
    val error: String
)

data class DeleteEventResponse(
    val status: Int,
    val message: String,
    val error: String? = null // Nullable to handle absence in success
)

data class EventEditResponse(
    val message: String,
    val data: EventData
)

data class EventData(
    val _id: String,
    val event_name: String,
    val description: String,
    val date: String,
    val time: String,
    val location: Location,
    val total_seats: Int,
    val ticketPrice: Int,
    val type: String,
    val artistname: String,
    val artistrole: String,
    val category: String,
    val validAge: Boolean,
    val images: List<String>,
    val videos: List<String>,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
)
{
data class Location(
    val city: String,
    val venue: String
)
}
