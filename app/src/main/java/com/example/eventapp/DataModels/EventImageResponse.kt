package com.example.eventapp.DataModels


//home folder
data class EventImagesResponse(
    val message: String,
    val images: List<EventImageData>
)

data class EventImageData(
    val _id: String,
    val images: List<String>
)

data class ArtistImageAndNameResponse(
    val message: String,
    val data: List<ArtistData>
)

data class ArtistData(
    val _id: String,
    val artistname: String,
    val artistrole: String,
    val images: List<String>
)
data class OrganizerDataResponse(
    val message: String,
    val data: OrganizerData2?
)

data class OrganizerData2(
    val _id: String?,
    val name: String?,
    val email: String?,
    val phone: String?,
)

data class UpcomingEventsResponse(
    val message: String,
    val data: List<UpcomingEvents>
)

data class UpcomingEvents(
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
)
{
data class Location(
    val venue: String,
    val city: String
)
    data class CategoryModel(
        val title: String,
        val iconRes: Int
    )


    data class ContactUsRequest(
        val first_name: String,
        val last_name: String,
        val email: String,
        val phone: Long,
        val servicetype: String,
        val message: String
    )

    data class ContactUsResponse(
        val message: String
    )

    data class EventSearchResponse(
        val message: String,
        val data: List<EventData>
    )


    data class SearchFilterResponse(
        val message: String,
        val data: SearchFilterData
    )

    data class SearchFilterData(
        val _id: String,
        val categories: List<String>,
        val venue: List<String>,
        val dates: List<String>
    )


    data class ServicesResponse(
        val message: String,
        val data: List<ServiceItem>
    )

    data class ServiceItem(
        val _id: String,
        val name: String,
        val description: String,
        val images: String, // You can prepend base URL later when loading the image
        val type: String,
        val isDeleted: Boolean,
        val createdAt: String,
        val updatedAt: String
    )

    data class PresentEventsResponse(
        val message: String,
        val data: List<PresentEvent>
    )

    data class PresentEvent(
        val _id: String,
        val event_name: String,
        val description: String,
        val date: String,
        val time: String,
        val location: EventLocation,
        val type: String,
        val online_link: String?,  // nullable
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
    )

    data class EventLocation(
        val venue: String,
        val city: String
    )

    data class AllTestimonialResponse(
        val data: List<TestimonialData>
    )
    data class TestimonialData(
        val _id: String,
        val commentdata: String,
        val rating: Int,
        val user_first_name: String,
        val user_last_name: String,
        val user_email: String
    )

}
