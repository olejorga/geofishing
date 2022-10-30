package no.hiof.geofishing.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import no.hiof.geofishing.ui.services.TodoNotificationService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            TodoNotificationService(context).apply {
                scheduleNotifications(true)
            }
        }
    }
}