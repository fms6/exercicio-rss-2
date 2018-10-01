package br.ufpe.cin.if710.rss

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat


class StaticBroadcastReceiver: BroadcastReceiver(){
    private val MY_NOTIFICATION_ID = 1
    private val NOTIFICATION_CHANNEL_ID = "br.ufpe.cin.if710.rss.notificacoes"

    override fun onReceive(context: Context, intent: Intent) {
        // Recebe o gerenciador de notificações
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Configura todos os parâmetros para notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "RSS Field Notification", IMPORTANCE_HIGH)
            )
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                0)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Feed RSS Atualizado")
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.alert_light_frame)

        // Notifica
        notificationManager.notify(MY_NOTIFICATION_ID, notification.build())
    }
}