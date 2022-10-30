package no.hiof.geofishing.ui.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.R
import no.hiof.geofishing.ui.receivers.TodoNotificationReceiver
import no.hiof.geofishing.ui.utils.NotificationChannelFactory

class TodoNotificationService(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "todo_channel"
        const val CHANNEL_NAME = "Todo"
        const val CHANNEL_DESCRIPTION = "Reminders to complete todos."
    }

    fun scheduleNotifications(runOnce: Boolean = false) {
        NotificationChannelFactory.create(CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION, context)

        CoroutineScope(Dispatchers.IO).launch {
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            val intents = ArrayList<PendingIntent>()

            NotificationChannelFactory.create(
                "todo_channel",
                "Todo",
                "Reminders to complete todos.",
                context
            )

            val app = context.applicationContext as App

            app.authService.id?.let {
                app.todoRepository
                    .search(
                        "profile",
                        it
                    )
                    .cancellable()
                    .collect { res ->
                        if (res.error == null && res.data != null) {
                            Log.d("HERE", "CHANGED")

                            for (intent in intents) {
                                alarmManager.cancel(intent)
                            }

                            intents.clear()

                            var id = 0

                            for (todo in res.data) {
                                if (!todo.completed && todo.reminder != null) {
                                    val intent = Intent(
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

                                    intents.add(intent)

                                    alarmManager.set(
                                        AlarmManager.RTC_WAKEUP,
                                        todo.reminder.time,
                                        intent
                                    )

                                    id++
                                }
                            }
                        }

                        if (runOnce) cancel()
                    }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification(id: Int, message: String) {
        val intent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.todoFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_done_24)
            .setContentTitle(CHANNEL_NAME)
            .setContentText(message)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(id, notification)
        }
    }
}