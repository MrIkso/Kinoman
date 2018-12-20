package ru.ratanov.kinoman.managers.firebase.push

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.ratanov.kinoman.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "FCM MESSAGE RECEIVED: ${remoteMessage.data}")
        showNotification(remoteMessage.data)
    }

    private fun showNotification(data: Map<String, String>) {
        val notification = NotificationCompat.Builder(applicationContext, "Kinoman")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Новая серия")
                .setContentText("Вышла новая серия сериала")
                .addAction(R.drawable.ic_notifications_black_24dp, "Скачать", null)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)

    }

}