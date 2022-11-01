package no.hiof.geofishing.ui.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

/**
 * A helper for creating notification channel if API 26+ (not necessary in older versions).
 */
object NotificationChannelFactory {
    fun create(id: String, name: String, description: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description

            with(NotificationManagerCompat.from(context)) {
                createNotificationChannel(channel)
            }
        }

    }
}