package com.example.simplesoup

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build

class OrderNotifier(appContext: Context) {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder

    private val channId = "com.example.simplesoup.notification"
    private val context = appContext

    fun makeNotification(title: String, details: String) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//Can't convert intent to Notification so we convert it to pending intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        //custom notification layout
        /*val contentView=RemoteViews(context.packageName,R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title,title)
        contentView.setTextViewText(R.id.tv_content,details)*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel.apply {
                NotificationChannel(channId, description, NotificationManager.IMPORTANCE_HIGH)
                enableLights(true)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(notificationChannel)



            builder = Notification.Builder(context, channId)
                .setContentTitle(title)
                .setContentText(details)
                // .setCustomContentView(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setStyle(Notification.BigTextStyle())
                .setSound(
                    Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                                context.packageName + "/" + R.raw.bettlemuse
                    ), AudioAttributes.USAGE_NOTIFICATION
                )
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(details)
                //.setContent(contentView)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setStyle(Notification.BigTextStyle())
                .setSound(
                    Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                                context.packageName + "/" + R.raw.bettlemuse
                    ), AudioAttributes.USAGE_NOTIFICATION
                )
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(123, builder.build())
    }
}