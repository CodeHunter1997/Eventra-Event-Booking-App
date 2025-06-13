package com.example.eventapp.DataModels


data class TestimonialRequest(
val commentdata: String,
val rating: Int
)
data class TestimonialResponse(
    val status: Int,
    val message: String
)

data class TestimonialDataUpdateRequest(
    val commentdata: String,
    val rating: Int
)
data class TestimonialDataUpdateResponse(
    val status: Int,
    val message: String
)

data class TestimonialDataShowResponse(
    val message: String,
    val error: String,
    val data: List<TestimonialDataSuccessfulResponse>?
)

data class TestimonialDataSuccessfulResponse(
    val commentdata: String,
    val rating: Int
)

data class TestimonialDeleteResponse(
    val message: String,
    val error: String,
    val data: TestimonialDeleteSuccessfulResponse? // Nullable because it may be missing on error
)

data class TestimonialDeleteSuccessfulResponse(
    val commentdata: String,
    val rating: Int
)