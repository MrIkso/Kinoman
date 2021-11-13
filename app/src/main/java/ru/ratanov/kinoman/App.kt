package ru.ratanov.kinoman

import android.app.Application
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.downloader.PRDownloader
import java.util.HashMap

class App : MultiDexApplication() {

    private val TAG = App::class.java.simpleName

    companion object {
        private var instance: Application? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        PRDownloader.initialize(this)
    }

}