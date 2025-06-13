package com.example.eventapp.User.MainFiles.HomePage.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.DataModels.TicketData
import com.example.eventapp.DataModels.TicketResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketViewModel(private val repository: TicketRepository) : ViewModel() {

    val ticketsLiveData = MutableLiveData<List<TicketData>?>()

    fun loadTickets() {
        repository.getBookedTickets().enqueue(object : Callback<TicketResponse> {
            override fun onResponse(call: Call<TicketResponse>, response: Response<TicketResponse>) {
                if (response.isSuccessful) {
                    ticketsLiveData.postValue(response.body()?.data)
                } else {
                    ticketsLiveData.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<TicketResponse>, t: Throwable) {
                ticketsLiveData.postValue(emptyList())
            }
        })
    }
}
