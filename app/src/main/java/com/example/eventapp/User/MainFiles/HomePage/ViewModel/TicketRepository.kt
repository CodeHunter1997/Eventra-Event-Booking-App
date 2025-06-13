package com.example.eventapp.User.MainFiles.HomePage.ViewModel

import android.content.Context
import com.example.eventapp.API_With_Object.ApiInterface
import com.example.eventapp.DataModels.TicketResponse
import retrofit2.Call


class TicketRepository(private val api: ApiInterface, private val context: Context) {

    fun getBookedTickets(): Call<TicketResponse> {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", "") ?: ""

        return api.getUserBookedTickets(token)
    }
}
