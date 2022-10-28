package no.hiof.geofishing.ui.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import no.hiof.geofishing.R

class TodoNotificationService(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun show() {
        val notification = NotificationCompat.Builder(context, TODO_CHANNEL_ID)
            .setSmallIcon(R.drawable.reshot_icon_blue_fish)
            .setContentTitle("Hello,")
            .setContentText("World!")
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val TODO_CHANNEL_ID = "todo_channel"
    }
}