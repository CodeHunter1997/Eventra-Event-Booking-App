package com.example.eventapp.DataModels

// Ticket list response
data class TicketListResponse(
    val status: Int,
    val message: String,
    val data: List<TicketData>
)

// Buy tickets
data class BuyTicketRequest(
    val event_id: String,
    val noOfTicketsBought: Int
)

data class BuyTicketResponse(
    val status: Int,
    val message: String,
    val data: TicketData  // Reusing TicketData
)

// Get ticket by ID
data class SingleTicketResponse(
    val status: Int,
    val message: String,
    val data: TicketDetail
)

data class TicketDetail(
    val ticketId: String,
    val eTicketId: String,
    val eventId: String,
    val quantity: Int,
    val eventDetails: TicketEventDetails,
    val attendeeId: String,
    val attendeeDetails: TicketAttendeeDetails,
    val issuedBy: String,
    val issuedAt: String,
    val isCancelled: Boolean,
    val version: Int
)

data class TicketEventDetails(
    val name: String,
    val location: TicketEventLocation,
    val date: String,
    val price: Int
)

data class TicketEventLocation(
    val venueName: String,
    val cityName: String
)

data class TicketAttendeeDetails(
    val fullName: String,
    val contact: TicketContact
)

data class TicketContact(
    val phoneNumber: String? = null,
    val emailAddress: String
)

// Cancel ticket
data class GenericResponse(
    val status: Int,
    val message: String,
    val error: String? = null
)

// User-booked ticket list
data class TicketResponse(
    val message: String,
    val data: List<TicketData>?,
    val status: Int,
    val error: String? = null

    )
data class CancelTicketResponse(
    val message: String,
    val data: TicketData?, // <- single object
    val status: Int,
    val error: String? = null
)


data class TicketData(
    val _id: String,
    val e_ticket_id: String,
    val event_id: String,
    val noOfTicketsBought: Int,
    val attendee_id: String,
    val sold_by: String,
    val sold_at: String,
    val isCancelled: Boolean,
    val event_info: EventInfo,
    val attendee_info: AttendeeInfo,
    var feedbackSubmitted: Boolean = false,
    var rating: Int? = null // nullable if rating might not exist yet

)

data class EventInfo(
    val event_name: String,
    val event_date: String,
    val price: Int,
    val event_location: EventLocation
)

data class EventLocation(
    val venue: String,
    val city: String
)

data class AttendeeInfo(
    val name: String,
    val contact_info: ContactInfo
)

data class ContactInfo(
    val phone: String,
    val email: String
)
