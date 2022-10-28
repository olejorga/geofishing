package no.hiof.geofishing.ui.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import no.hiof.geofishing.App
import no.hiof.geofishing.R

class TodoNotificationService : Service() {
    private val channelId = "todo_channel"
    private lateinit var job: CompletableJob

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Todo",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders to complete todos."
            }

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }


    private fun showNotification(text: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_done_24)
            .setContentTitle("Todo")
            .setContentText(text)
            .build()

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .notify(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d("HERE", "Service started.")

        createChannel()

        job = Job()

        CoroutineScope(Dispatchers.IO + job).launch {
            (applicationContext as App)
                .todoRepository
                .search(
                    "profile",
                    (applicationContext as App).authService.id!!
                ).collect { res ->
                    if (res.error == null && res.data != null) {
                        Log.d("HERE", "Service data fetched.")

                        for (todo in res.data) {
                            if (!todo.completed) {
                                Log.d("HERE", todo.reminder.toString())
                                todo.description?.let { showNotification(it) }
                            }
                        }
                    }
                }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}