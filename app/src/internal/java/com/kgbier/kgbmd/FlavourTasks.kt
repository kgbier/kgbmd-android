package com.kgbier.kgbmd

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.dev.DeveloperMenuActivity

private const val CHANNEL_ID = "DEV_NOTIFICATION_CHANNEL_ID"
private const val NOTIFICATION_ID = 451

private var isChannelCreated = false

fun developerMenuNotificationTask(context: Context) {
    if (!isChannelCreated) {
        createNotificationChannel(context)
    }

    val intent = Intent(context, DeveloperMenuActivity::class.java)
    val devNotificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_dev)
        .setContentTitle("Developer menu")
        .setContentText("Open the developer menu")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))

    with(NotificationManagerCompat.from(context)) {
        notify(NOTIFICATION_ID, devNotificationBuilder.build())
    }
}

private fun createNotificationChannel(context: Context) {
    isChannelCreated = true

    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Developer Menu"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
