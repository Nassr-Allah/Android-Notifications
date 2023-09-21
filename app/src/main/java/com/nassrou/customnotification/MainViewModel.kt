package com.nassrou.customnotification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val webSocket: WebSocket = WebSocket()) : ViewModel() {

    var message = mutableStateOf("Hello World!")
        private set

    fun updateMessage(text: String) {
        message.value = text
    }

    fun sendMessage() {
        viewModelScope.launch {
            webSocket.sendMessage(message.value)
        }
    }

    fun initSession() {
        viewModelScope.launch {
            webSocket.initSession()
        }
    }

    fun observeMessages() {
        viewModelScope.launch {
            webSocket.observeMessages().collect {
                Log.d("WebSocket", "collected: $it")
            }
        }
    }

}