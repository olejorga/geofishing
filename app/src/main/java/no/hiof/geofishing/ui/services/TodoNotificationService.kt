package no.hiof.geofishing.ui.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.ui.receivers.TodoNotificationReceiver
import no.hiof.geofishing.ui.utils.NotificationChannelFactory

/**
 * Responsible for scheduling todos.
 */
class TodoNotificationService : LifecycleService() {
    companion object {
        const val CHANNEL_ID = "todo_channel"
        const val CHANNEL_NAME = "Todo"
        const val CHANNEL_DESCRIPTION = "Reminders to complete todos."
    }

    /**
     * When the service starts, a coroutine is created in which the todos a fetched from
     * the globally injected todoRepository. The todos are iterated over and a alarm with a
     * notification is scheduled for all todos.
     *
     * The coroutine remains open and listening for new data until the lifecycle is terminated.
     * For instance on device startup, when the "BootReceiver" is done running, this service is
     * terminated.
     *
     * If the todos are updated, all alarms are canceled and re-scheduled.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val context = this

        NotificationChannelFactory.create(
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION, context
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            val intents = ArrayList<PendingIntent>()

            val app = context.applicationContext as GeofishingApplication

            app.authService.id?.let {
                app.todoRepository
                    .search(
                        "profile",
                        it
                    )
                    .cancellable()
                    .collect { res ->
                        if (res.error == null && res.data != null) {
                            for (alarm in intents) {
                                alarmManager.cancel(alarm)
                            }

                            intents.clear()

                            var id = 0

                            for (todo in res.data) {
                                if (!todo.completed && todo.reminder != null) {
                                    val alarm = Intent(
                                        context,
                                        TodoNotificationReceiver::class.java
                                    ).apply {
                                        putExtra("id", id)
                                        putExtra("message", todo.description)
                                    }.let { intent ->
                                        PendingIntent.getBroadcast(
                                            context,
                                            id,
                                            intent,
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                                PendingIntent.FLAG_IMMUTABLE
                                            else 0
                                        )
                                    }

                                    intents.add(alarm)

                                    alarmManager.set(
                                        AlarmManager.RTC_WAKEUP,
                                        todo.reminder.time,
                                        alarm
                                    )

                                    id++
                                }
                            }
                        }
                    }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}