package com.example.eventapp.API_With_Object


import com.example.eventapp.Auth.DataModels.ChangePasswordRequest
import com.example.eventapp.Auth.DataModels.ChangePasswordResponse
import com.example.eventapp.Auth.DataModels.DeleteAccountRequest
import com.example.eventapp.Auth.DataModels.DeleteAccountResponse
import com.example.eventapp.Auth.DataModels.ForgetEmailRequest
import com.example.eventapp.Auth.DataModels.ForgetEmailResponse
import com.example.eventapp.Auth.DataModels.LoginDataModelRequest
import com.example.eventapp.Auth.DataModels.LoginResponse
import com.example.eventapp.Auth.DataModels.NewPasswordRequest
import com.example.eventapp.Auth.DataModels.NewPasswordResponse
import com.example.eventapp.Auth.DataModels.OrganizerForgetEmailRequest
import com.example.eventapp.Auth.DataModels.OrganizerForgetEmailResponse
import com.example.eventapp.Auth.DataModels.OrganizerLoginDataModelRequest
import com.example.eventapp.Auth.DataModels.OrganizerLoginResponse
import com.example.eventapp.Auth.DataModels.OrganizerNewPasswordRequest
import com.example.eventapp.Auth.DataModels.OrganizerNewPasswordResponse
import com.example.eventapp.Auth.DataModels.OrganizerOtpSendRequest
import com.example.eventapp.Auth.DataModels.OrganizerOtpSendResponse
import com.example.eventapp.Auth.DataModels.OrganizerOtpVerificationRequest
import com.example.eventapp.Auth.DataModels.OrganizerOtpVerificationResponse
import com.example.eventapp.Auth.DataModels.OrganizerSignUpDataModel
import com.example.eventapp.Auth.DataModels.OrganizerSignUpDataModelResponse
import com.example.eventapp.Auth.DataModels.OtpSendRequest
import com.example.eventapp.Auth.DataModels.OtpSendResponse
import com.example.eventapp.Auth.DataModels.OtpVerificationRequest
import com.example.eventapp.Auth.DataModels.OtpVerificationResponse
import com.example.eventapp.Auth.DataModels.SignUpDataModel
import com.example.eventapp.Auth.DataModels.SignUpResponse
import com.example.eventapp.DataModels.ApiResponse
import com.example.eventapp.DataModels.ArtistImageAndNameResponse
import com.example.eventapp.DataModels.BuyTicketRequest
import com.example.eventapp.DataModels.BuyTicketResponse
import com.example.eventapp.DataModels.CancelTicketResponse
import com.example.eventapp.DataModels.CreateEventResponse
import com.example.eventapp.DataModels.DashboardResponse
import com.example.eventapp.DataModels.DeleteEventResponse
import com.example.eventapp.DataModels.EventEditResponse
import com.example.eventapp.DataModels.EventImagesResponse
import com.example.eventapp.DataModels.EventListResponse
import com.example.eventapp.DataModels.EventUpdateResponse
import com.example.eventapp.DataModels.GenericResponse
import com.example.eventapp.DataModels.OrganizerDashboardResponse
import com.example.eventapp.DataModels.OrganizerDataResponse
import com.example.eventapp.DataModels.OrganizerEventListResponse
import com.example.eventapp.DataModels.OrganizerListResponse
import com.example.eventapp.DataModels.OrganizerProfileUpdateRequest
import com.example.eventapp.DataModels.OrganizerProfileUpdateResponse
import com.example.eventapp.DataModels.OrganizerResponse
import com.example.eventapp.DataModels.SingleTicketResponse
import com.example.eventapp.DataModels.TestimonialDataShowResponse
import com.example.eventapp.DataModels.TestimonialDataUpdateRequest
import com.example.eventapp.DataModels.TestimonialDataUpdateResponse
import com.example.eventapp.DataModels.TestimonialDeleteResponse
import com.example.eventapp.DataModels.TestimonialRequest
import com.example.eventapp.DataModels.TestimonialResponse
import com.example.eventapp.DataModels.TicketListResponse
import com.example.eventapp.DataModels.TicketResponse
import com.example.eventapp.DataModels.UpcomingEvent
import com.example.eventapp.DataModels.UpcomingEvents.AllTestimonialResponse
import com.example.eventapp.DataModels.UpcomingEvents.ContactUsRequest
import com.example.eventapp.DataModels.UpcomingEvents.ContactUsResponse
import com.example.eventapp.DataModels.UpcomingEvents.EventSearchResponse
import com.example.eventapp.DataModels.UpcomingEvents.PresentEventsResponse
import com.example.eventapp.DataModels.UpcomingEvents.SearchFilterResponse
import com.example.eventapp.DataModels.UpcomingEvents.ServicesResponse
import com.example.eventapp.DataModels.UpcomingEventsResponse
import com.example.eventapp.DataModels.UpdateEventRequest
import com.example.eventapp.DataModels.UserBookedTicketListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
//user
    @POST("user/login")
    fun loginUser(@Body request: LoginDataModelRequest): Call<LoginResponse>

    @POST("user/register")
    fun registerUser(@Body request: SignUpDataModel): Call<SignUpResponse>

    @POST("user/otpsend")
    fun otpSend(@Body request: OtpSendRequest): Call<OtpSendResponse>

    @POST("user/otpverify")
    fun otpVerify(@Body request: OtpVerificationRequest): Call<OtpVerificationResponse>

    @POST("user/forgorpassword")
    fun newPassword(@Header("x-access-token") token: String, @Body request: NewPasswordRequest): Call<NewPasswordResponse>

    @POST("user/forgorpassword-email-send")
    fun forgetPassword(@Body request: ForgetEmailRequest): Call<ForgetEmailResponse>

    @POST("user/deleteaccount")
    fun deleteAccount(@Header("x-access-token") token: String, @Body request: DeleteAccountRequest): Call<DeleteAccountResponse>


    @POST("user/testimonialdatastore/{eventId}")
    fun testimonialDataStore(
        @Header("x-access-token") token: String,
        @Path("eventId") eventId: String,
        @Body request: TestimonialRequest
    ): Call<TestimonialResponse>


    @POST("user/testimonialdataupdate/{userId}")
    fun testimonialDataUpdate(
        @Path("userId") userId: String,
        @Body request: TestimonialDataUpdateRequest,
        @Header("x-access-token") token: String
    ): Call<TestimonialDataUpdateResponse>



//event data

    @GET("user/testimonialdatashow/{userId}")
    fun getTestimonialDataShow(
        @Path("userId") userId: String,
        @Header("x-access-token") token: String
    ): Call<TestimonialDataShowResponse>

    @GET("user/testimonialdatadelete/{userId}")
    fun getTestimonialDelete(
        @Path("userId") userId: String,
        @Header("x-access-token") token: String
    ): Call<TestimonialDeleteResponse>

    @POST("user/changepassword")
    fun changePassword(
        @Header("x-access-token") token: String,
        @Body request: ChangePasswordRequest
    ): Call<ChangePasswordResponse>

//    @GET("user/user-booked-tickets")
//    fun getUserBookedTickets(
//        @Header("x-access-token") token: String
//    ): Call<UserBookedTicketListResponse>

    @GET("user/dashboard")
    fun getUserDashboard(
        @Header("x-access-token") token: String
    ): Call<DashboardResponse>

    @GET("user/user-booked-tickets")
    fun getUserBookedTickets(
        @Header("x-access-token") token: String
    ): Call<TicketResponse>

//Organizer

    @POST("organizer/register")
    fun registerOrganizer(@Body request: OrganizerSignUpDataModel): Call<OrganizerSignUpDataModelResponse>

    @POST("organizer/login")
    fun loginOrganizer(@Body request: OrganizerLoginDataModelRequest): Call<OrganizerLoginResponse>

    @POST("organizer/otpsend")
    fun organizerOtpSend(@Body request: OrganizerOtpSendRequest): Call<OrganizerOtpSendResponse>

    @POST("organizer/otpverify")
    fun organizerOtpVerify(@Body request: OrganizerOtpVerificationRequest): Call<OrganizerOtpVerificationResponse>

    @POST("organizer/forgorpassword")
    fun organizerNewPassword(@Header("x-access-token") token: String, @Body request: OrganizerNewPasswordRequest): Call<OrganizerNewPasswordResponse>

    @POST("organizer/forgorpassword-email-send")
    fun organizerForgetPassword(@Body request: OrganizerForgetEmailRequest): Call<OrganizerForgetEmailResponse>

    @POST("organizer/deleteaccount")
    fun organizerDeleteAccount(@Header("x-access-token") token: String, @Body request: DeleteAccountRequest): Call<DeleteAccountResponse>

    @POST("organizer/profile-update")
    suspend  fun organizerProfileUpdate(@Header("x-access-token") token: String, @Body request: OrganizerProfileUpdateRequest): Call<OrganizerProfileUpdateResponse>

    @GET("organizer/event-list-by-organizer-id")
    suspend fun organizerEventList(
        @Header("x-access-token") token: String
    ): Response<OrganizerEventListResponse>


    @GET("organizer/dashboard")
    suspend fun organizerDashboard(
        @Header("x-access-token") token: String
    ): Response<OrganizerDashboardResponse>

    @GET("getorganizerdata")
    suspend fun getOrganizers(): Response<OrganizerResponse>


    @GET("organizer/list")
    suspend fun organizerList(): Response<OrganizerListResponse>


//events

    @GET("event/list")
    suspend fun getEventList(): Response<EventListResponse>

    @FormUrlEncoded
    @POST("event/create")
    fun createEvent(
        @Header("x-access-token") token: String,
        @Field("event_name") eventName: String,
        @Field("description") description: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("city") city: String,
        @Field("venue") venue: String,
        @Field("total_seats") totalSeats: Int,
        @Field("ticketPrice") ticketPrice: Int,
        @Field("type") type: String, // Make sure it's either "online" or "offline"
        @Field("artistname") artistName: String,
        @Field("artistrole") artistRole: String,
        @Field("category") category: String
    ): Call<CreateEventResponse>




    @PUT("event/update/{eventId}")
    fun updateEvent(
        @Header("x-access-token") token: String,
        @Path("eventId") eventId: String,
        @Body event: UpdateEventRequest // or CreateEventRequest
    ): Call<EventUpdateResponse>

    @GET("event/delete/{eventId}")
    fun deleteEvent(
        @Header("x-access-token") token: String,
        @Path("eventId") eventId: String
    ): Call<DeleteEventResponse>

    @GET("event/edit/{eventId}")
    fun getEventDetailsForEdit(
        @Header("x-access-token") token: String,
        @Path("eventId") eventId: String
    ): Call<EventEditResponse>


    @GET("upcomingeventsdata")
    suspend fun getUpcomingEvents(): Response<ApiResponse<List<UpcomingEvent>>>

//Ticket

    @POST("ticket/create")
    fun buyTicket(
        @Header("x-access-token") token: String,
        @Body buyTicketRequest: BuyTicketRequest
    ): Call<BuyTicketResponse>


    @GET("ticket/list")
    fun getTicketList(
        @Header("x-access-token") token: String
    ): Call<TicketListResponse>

        @GET("ticket/getById/{ticketId}")
        fun getTicketById(
            @Path("ticketId") ticketId: String,
            @Header("x-access-token") token: String
        ): Call<SingleTicketResponse>


        @POST("ticket/cancel/{ticketId}")
        fun cancelTicket(
            @Header("x-access-token") token: String,
            @Path("ticketId") ticketId: String
        ): Call<CancelTicketResponse>




//Home


        @GET("eventimages")
        fun getEventImages(): Call<EventImagesResponse>

        @GET("artistimageandname")
        fun getArtistImageAndName(): Call<ArtistImageAndNameResponse>

        @GET("getorganizerdata")
        fun getOrganizerData(
            @Header("x-access-token") token: String // Optional
        ): Call<OrganizerDataResponse>

        @GET("upcomingeventsdata")
        fun getUpcomingEvents2(): Call<UpcomingEventsResponse>

        @POST("contactusdatastore")
        fun submitContactUs(
            @Body contactUsRequest: ContactUsRequest
        ): Call<ContactUsResponse>

        @GET("eventsearch")
        fun searchEventByCategory(
            @Query("catagory") category: String
        ): Call<EventSearchResponse>

        @GET("eventdatadisplayforsearch")
        fun getSearchFilterData(): Call<SearchFilterResponse>

        @GET("servicesdata")
        fun getServicesData(): Call<ServicesResponse>

        @GET("presenteventsdata")
        fun getPresentEvents(): Call<PresentEventsResponse>

        @GET("singleevent/{id}")
        fun getSingleEvent(
            @Path("id") eventId: String
        ): Call<PresentEventsResponse>


    @GET("testimonialdata")
    suspend fun getTestimonials(): AllTestimonialResponse



    //admin
    @POST("admin/login")
    fun loginAdmin(@Body request: LoginDataModelRequest): Call<LoginResponse>
}
