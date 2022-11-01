package no.hiof.geofishing.ui.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.ui.receivers.TodoNotificationReceiver
import no.hiof.geofishing.ui.utils.NotificationChannelFactory

class TodoNotificationService : LifecycleService() {
    companion object {
        const val CHANNEL_ID = "todo_channel"
        const val CHANNEL_NAME = "Todo"
        const val CHANNEL_DESCRIPTION = "Reminders to complete todos."
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d("HERE", "STARTING SERVICE")

        val context = this

        NotificationChannelFactory.create(
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION, context
        )

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("HERE", "STARTING SCOPE")
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            val intents = ArrayList<PendingIntent>()

            NotificationChannelFactory.create(
                "todo_channel",
                "Todo",
                "Reminders to complete todos.",
                context
            )

            val app = context.applicationContext as App

            Log.d("HERE", app.authService.id.toString())

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

                            Log.d("HERE", "TODOS UPDATED!")
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HERE", "STOPPING SERVICE")
    }
}