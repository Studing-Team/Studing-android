package com.team.studing.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team.studing.MainActivity
import com.team.studing.R

class FirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "onNewToken: ${token} ")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("##", "onMessageReceived: ${message.from} ")
        message.notification?.let {
            showNotification(
                messageTitle = it.title ?: "",
                messageBody = it.body ?: "",
                messageType = message.data["type"].toString(),
                messageNoticeId = message.data["noticeId"].toString()
            )
        }
    }

    private fun showNotification(
        messageTitle: String,
        messageBody: String,
        messageType: String,
        messageNoticeId: String
    ) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = (System.currentTimeMillis() / 7).toInt() // 고유 ID 지정

        createNotificationChannel(notificationManager)
        Log.d("##", "onMessageReceived: ${messageType}, ${messageNoticeId} ")

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("type", messageType)
            putExtra("noticeId", messageNoticeId)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationManager.notify(
            notificationID,
            notificationBuilder(messageTitle, messageBody, pendingIntent)
        )

    }

    private fun notificationBuilder(title: String, body: String, pendingIntent: PendingIntent) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_logo_foreground
                )
            )
            .setSmallIcon(R.mipmap.ic_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()


    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_NAME = "StudingNotification"
        private const val CHANNEL_DESCRIPTION = "Channel For Studing Notification"
        private const val CHANNEL_ID = "fcm_default_channel"
    }
}
