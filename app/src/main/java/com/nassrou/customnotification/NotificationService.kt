package com.nassrou.customnotification

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class NotificationService(private val webSocket: WebSocket = WebSocket()) : Service(), CoroutineScope by MainScope() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "START_SERVICE") {
            val notification = NotificationCompat.Builder(this, "notification_channel").build()

            startForeground(1, notification)

            launch {
                while (true) {
                    Log.d("NotificationService", "running...")
                    delay(2000)
                }
            }
        }

        if (intent?.action == "MESSAGE") {
            val notification = NotificationCompat.Builder(this, "notification_channel").build()
            startForeground(1, notification)

            launch {
                // webSocket.initSession()
                webSocket.observeMessages().collect { message ->
                    with(NotificationManagerCompat.from(this@NotificationService)) {
                        notify(Random(1000).nextInt(), createNotification(message))
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, "received_notification")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Server")
            .setContentText(text)
            .build()
    }
}
