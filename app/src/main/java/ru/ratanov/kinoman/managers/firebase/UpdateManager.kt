package ru.ratanov.kinoman.managers.firebase

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import ru.ratanov.kinoman.App
import java.io.File

object UpdateManager {

    fun update(updateUrl: String) {
        getPath().mkdirs()

        PRDownloader.download(updateUrl, getPath().absolutePath, "update.apk")
                .build()
                .start(object: OnDownloadListener {
                    override fun onDownloadComplete() {
                        install(File(getPath().absolutePath + "/update.apk"))
                    }

                    override fun onError(error: Error?) {
                        Toast.makeText(App.instance(), "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getPath() = File(App.instance().getExternalFilesDir(null), "Update")

    private fun install(file: File) {
        val context = App.instance()
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }
}