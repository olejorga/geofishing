package no.hiof.geofishing.ui.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.R
import no.hiof.geofishing.ui.services.TodoNotificationService

/**
 * Responsible for showing a notification when a broadcast is received.
 *
 * The intent passed in the broadcast should contain extras, an "id" and a "message":
 * - id (int): The desired id of the notification (defaults to 0).
 * - message (string): The text that will be displayed in the notification (defaults to a std msg).
 */
class TodoNotificationReceiver : BroadcastReceiver() {
    /**
     * Displays a notification reminder when a broadcast is received.
     *
     * @property context A derived application context.
     * @property intent A intent (should contain an "id" (int) and a "message" (string)).
     */
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", 0)
        val message = intent.getStringExtra("message")
            ?: context.getString(R.string.todo_notification_receiver_message_placeholder)

        val action = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.todoFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, TodoNotificationService.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_done_24)
            .setContentTitle(TodoNotificationService.CHANNEL_NAME)
            .setContentText(message)
            .setContentIntent(action)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(id, notification)
        }
    }
}