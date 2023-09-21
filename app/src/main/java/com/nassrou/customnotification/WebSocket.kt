package com.nassrou.customnotification

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class WebSocket {

    companion object {
        private const val BASE_URL = "ws://192.168.1.8:8080"
        private val ktorClient = HttpClient {
            install(WebSockets)
        }
        var socket: WebSocketSession? = null
    }

    suspend fun initSession() {
        try {
            Log.d("WebSocket", "starting session...")
            if (socket == null) {
                socket = ktorClient.webSocketSession {
                    url("$BASE_URL/chat/1/1")
                }
            }
            Log.d("WebSocket", "session started with socket: $socket")
        } catch (e: Exception) {
            Log.d("WebSocket", e.localizedMessage ?: "Null exception at initSession()")
            e.printStackTrace()
        }
    }

    suspend fun sendMessage(message: String) {
        try {
            Log.d("WebSocket", "sending message...")
            socket?.send(Frame.Text(message))
            Log.d("WebSocket", "message sent!")
        } catch (e: Exception) {
            Log.d("WebSocket", e.localizedMessage ?: "Null exception at initSession()")
            e.printStackTrace()
        }
    }

    suspend fun observeMessages(): Flow<String> = flow {
        try {
            Log.d("WebSocket", "called observeMessages()")
            if (socket != null) {
                Log.d("WebSocket", "socket not null")
                for (frame in socket!!.incoming) {
                    frame as? Frame.Text ?: continue
                    Log.d("WebSocket", "received: ${frame.readText()}")
                    val message = frame.readText()
                    emit(message)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}