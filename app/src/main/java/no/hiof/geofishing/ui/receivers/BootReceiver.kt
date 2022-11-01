package no.hiof.geofishing.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import no.hiof.geofishing.ui.services.TodoNotificationService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action == "android.intent.action.BOOT_COMPLETED" ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            Intent(context, TodoNotificationService::class.java).also {
                context.startService(it)
            }
        }
    }
}