package ru.ratanov.kinoman.managers.firebase.push

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.ratanov.kinoman.R
import ru.ratanov.kinoman.ui.activity.main.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "FCM MESSAGE RECEIVED: ${remoteMessage.data}")
        showNotification(remoteMessage.data)
    }

    private fun showNotification(data: Map<String, String>) {
        val sound = Uri.parse("android.resource://${applicationContext.packageName}/${R.raw.incoming_message}")

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(applicationContext, "Kinoman")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Новая серия")
                .setContentText("Улица (3 сезон: 1-66 серии из 66)")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Улица (3 сезон: 1-66 серии из 66)"))
                .addAction(R.drawable.ic_notifications_black_24dp, "Скачать", pendingIntent)
                .addAction(R.drawable.ic_notifications_black_24dp, "Открыть", null)
                .setSound(sound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        builder.color = ContextCompat.getColor(applicationContext, R.color.blue_dark)
        val notification = builder.build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

    }

}