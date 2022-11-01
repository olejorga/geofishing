package no.hiof.geofishing.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import no.hiof.geofishing.ui.services.TodoNotificationService

/**
 * Responsible for running tasks when the device starts.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Makes sure that tasks are not called unintentionally by other intents.
        if (
            intent.action == "android.intent.action.BOOT_COMPLETED" ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            // Checks for notifications (once) on startup as a "background service".
            // It does not continue to run when the app is not in the foreground.
            // It will be destroyed when the intent action completes.
            Intent(context, TodoNotificationService::class.java).also {
                context.startService(it)
            }
        }
    }
}