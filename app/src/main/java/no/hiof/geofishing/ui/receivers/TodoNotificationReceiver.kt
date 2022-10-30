package no.hiof.geofishing.ui.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import no.hiof.geofishing.ui.services.TodoNotificationService

class TodoNotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", 0)
        val message = intent.getStringExtra("message")

        TodoNotificationService(context).apply {
            showNotification(id, message ?: "You have something to do.")
        }
    }
}