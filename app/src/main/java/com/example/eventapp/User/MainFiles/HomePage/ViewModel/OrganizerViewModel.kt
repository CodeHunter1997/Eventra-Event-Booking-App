package com.example.eventapp.User.MainFiles.HomePage.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.Organizer
import kotlinx.coroutines.launch

class OrganizerViewModel : ViewModel() {

    private val _organizerList = MutableLiveData<List<Organizer>>()
    val organizerList: LiveData<List<Organizer>> = _organizerList

    fun fetchOrganizers() {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.organizerList()
                if (response.isSuccessful) {
                    _organizerList.value = response.body()?.data ?: emptyList()
                } else {
                    Log.e("OrganizerViewModel", "Error fetching organizers: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("OrganizerViewModel", "Exception fetching organizers: ${e.message}")
            }
        }
    }
}