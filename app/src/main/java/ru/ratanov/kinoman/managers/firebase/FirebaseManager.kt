package ru.ratanov.kinoman.managers.firebase

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.*

object FirebaseManager {

    private val TAG = FirebaseManager.javaClass.simpleName

    fun init() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = "1.0.0"
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] = "https://play.google.com/store/apps/details?id=com.sembozdemir.renstagram"

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(60)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "remote config is fetched")
                        firebaseRemoteConfig.activateFetched()
                    }
                }
    }
}