package com.example.standup

import AlarmReceiver
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {
    private val NOTIFICATION_ID = 0
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"

    private var mNotificationManager: NotificationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val alarmToggle: ToggleButton = findViewById(R.id.alarmToggle)
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        val alarmUp = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID,
            notifyIntent, PendingIntent.FLAG_NO_CREATE
        ) != null
        alarmToggle.isChecked = alarmUp
        val notifyPendingIntent = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmToggle.setOnCheckedChangeListener { buttonView, isChecked ->
            val toastMessage: String
            toastMessage = if (isChecked) {
                val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
                val triggerTime = (SystemClock.elapsedRealtime()
                        + repeatInterval)

                alarmManager?.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, repeatInterval,
                    notifyPendingIntent
                )

                getString(R.string.alarm_on_toast)
            } else {
                mNotificationManager!!.cancelAll()
                alarmManager?.cancel(notifyPendingIntent)
                getString(R.string.alarm_off_toast)
            }

            Toast.makeText(
                this@MainActivity, toastMessage,
                Toast.LENGTH_SHORT
            ).show()
        }


        createNotificationChannel()
    }

    fun createNotificationChannel() {
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {


            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifies every 15 minutes to " +
                    "stand up and walk"
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
    }
}
