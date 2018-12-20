package ru.ratanov.kinoman.managers.firebase.push

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    private val TAG = MyFirebaseInstanceIdService::class.java.simpleName

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val fcmToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "FCM Token: $fcmToken")

        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document("admin")
        userDocRef
                .update("fcmToken", fcmToken)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }
}