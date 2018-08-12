package ru.ratanov.kinoman

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import ru.ratanov.kinoman.managers.firebase.FirebaseManager
import ru.ratanov.kinoman.managers.firebase.ForceUpdateChecker
import java.util.HashMap

class App : Application() {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
//        FirebaseManager.init()

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = "0.0.0"
        remoteConfigDefaults[ForceUpdateChecker.KEY_RELEASE_NOTES] = "release notes"
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