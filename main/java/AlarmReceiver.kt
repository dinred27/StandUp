import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.standup.MainActivity
import com.example.standup.R

class AlarmReceiver : BroadcastReceiver() {
    private val NOTIFICATION_ID = 0
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private var mNotificationManager: NotificationManager? = null
    override fun onReceive(context: Context, intent: Intent) {
        mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stand_up)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        mNotificationManager!!.notify(NOTIFICATION_ID, builder.build())
    }

}
