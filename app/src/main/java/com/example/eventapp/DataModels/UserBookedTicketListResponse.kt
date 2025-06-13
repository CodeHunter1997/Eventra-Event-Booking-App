package com.example.eventapp.DataModels


data class UserBookedTicketListResponse(
    val message: String,
    val data: List<Ticket>
)

data class Ticket(
    val _id: String,
    val e_ticket_id: String,
    val event_id: String,
    val noOfTicketsBought: Int,
    val attendee_id: String,
    val sold_by: String,
    val sold_at: String,
    val isCancelled: Boolean,
    val event_info: EventInfo,
    val attendee_info: AttendeeInfo
)

data class EventInfo2(
    val event_name: String,
    val event_date: String,
    val price: Int,
    val event_location: EventLocation2
)

data class EventLocation2(
    val venue: String,
    val city: String
)

data class AttendeeInfo2(
    val name: String,
    val contact_info: ContactInfo
)

data class ContactInfo2(
    val phone: String,
    val email: String
)

data class DashboardResponse(
    val message: String,
    val data: AttendeeData
)


data class AttendeeData(
    val _id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: Long,
    val role: String,
    val gender: String,
    val dob: String,
    val createdAt: String
)
