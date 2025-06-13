package com.example.eventapp.User.MainFiles.HomePage.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.EventMinimal
import kotlinx.coroutines.launch

class EventViewModel2 : ViewModel() {
    private val _eventList = MutableLiveData<List<EventMinimal>>()
    val eventList: LiveData<List<EventMinimal>> get() = _eventList

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getEventList()
                if (response.isSuccessful && response.body() != null) {
                    _eventList.value = response.body()!!.data
                } else {
                    Log.e("EventViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("EventViewModel", "Exception: ${e.message}")
            }
        }
    }
}
