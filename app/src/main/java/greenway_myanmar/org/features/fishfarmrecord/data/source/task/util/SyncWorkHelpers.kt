package greenway_myanmar.org.features.fishfarmrecord.data.source.task.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import greenway_myanmar.org.R

private const val SYNC_CHANNEL_ID = "sync_data"
private const val SyncNotificationId = 0
/**
 * Foreground information for sync on lower API levels when sync workers are being run with a
 * foreground service
 */
fun Context.syncForegroundInfo() = ForegroundInfo(
    SyncNotificationId,
    syncNotification(),
)

fun Context.syncNotification(): Notification {
    createNotificationChannel()
    return NotificationCompat.Builder(this, SYNC_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_stat_notification)
        .setContentTitle("Syncing")
        .setContentText("Syncing data to server")
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Syncing data to server")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}

private fun Context.createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "အစိမ်းရောင်လမ်း"
        val descriptionText = "အစိမ်းရောင်လမ်းမှ လုပ်ဆောင်နေမှု အသိပေးချက်"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(SYNC_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
