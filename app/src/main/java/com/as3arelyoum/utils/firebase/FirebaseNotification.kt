package com.as3arelyoum.utils.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.as3arelyoum.ui.activity.LauncherActivity
import com.as3arelyoum.utils.Constants.CHANNEL_ID
import com.as3arelyoum.utils.PrefUtil
import com.as3arelyoum.utils.PrefUtil.initPrefUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotification : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        initPrefUtil(this)
        if (token.isNotEmpty()) {
            PrefUtil.saveData("token", token)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title
        val body = remoteMessage.data["body"] ?: remoteMessage.notification?.body

        val notificationIntent = Intent(this, LauncherActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        notificationIntent.putExtra("title", title)
        notificationIntent.putExtra("body", body)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Notification.Builder(this, CHANNEL_ID)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setLights(NotificationCompat.FLAG_SHOW_LIGHTS, 3000, 1000)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle())
            .setColor(Color.BLUE)
            .setLights(Color.BLUE, 3000, 1000)
            .build()

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "FCM Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.lightColor = Color.WHITE
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notification)
    }
}