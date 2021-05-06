package com.sokrat.serviceapplication

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "com.sokrat.serviceapplication.NotificationChannel_Id"
const val ACTION_MEDIA_PLAYER_PLAY = "com.sokrat.serviceapplication.ACTION_MEDIA_PLAYER_PLAY"
const val ACTION_MEDIA_PLAYER_NEXT = "com.sokrat.serviceapplication.ACTION_MEDIA_PLAYER_NEXT"
const val ACTION_MEDIA_PLAYER_PREVIOUS = "com.sokrat.serviceapplication.ACTION_MEDIA_PLAYER_PREVIOUS"
const val REQUEST_CODE = 1999
class MediaPlayerService : Service() {
    private var mediaplayer: MediaPlayer? = null
    private val songManager = SongManager()
    private var currentSong = songManager.getNextSong()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(CHANNEL_ID,"Service app", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Some description"
        channel.setSound(null, null)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        mediaplayer = MediaPlayer.create(this, currentSong.id)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var notification = getNotification()


        if (intent?.getBooleanExtra("Play_Pause", false)?: false){
            mediaplayer?.pause()
        } else {
            mediaplayer?.start()
        }
        if (intent?.getBooleanExtra("Next", false)?: false){
            currentSong = songManager.getNextSong()
            mediaplayer?.release()
            mediaplayer = MediaPlayer.create(this, currentSong.id)
            mediaplayer?.start()
        }

        if (intent?.getBooleanExtra("Previous", false)?: false){
            currentSong = songManager.getPreviousSong()
            mediaplayer?.release()
            mediaplayer = MediaPlayer.create(this, currentSong.id)
            mediaplayer?.start()
        }
        notification = getNotification()
        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mediaplayer?.stop()
        mediaplayer?.release()
        mediaplayer = null

        super.onDestroy()
        Log.d("getNotify", "OnDestroy")
    }


    private fun getNotification(): Notification? {



        val intent = Intent(ACTION_MEDIA_PLAYER_PLAY)
        intent.putExtra("Play_Pause", mediaplayer?.isPlaying)
        intent.putExtra("Next", false)
        intent.putExtra("Previous", false)
        val pendingIntent = PendingIntent.getService(this, REQUEST_CODE, intent,PendingIntent.FLAG_CANCEL_CURRENT)

        val nextIntent = Intent(ACTION_MEDIA_PLAYER_NEXT)
        nextIntent.putExtra("Next", true)
        nextIntent.putExtra("Play_Pause", mediaplayer?.isPlaying)
        nextIntent.putExtra("Previous", false)
        val nextPendingIntent = PendingIntent.getService(this, REQUEST_CODE, nextIntent,PendingIntent.FLAG_CANCEL_CURRENT)

        val previousIntent = Intent(ACTION_MEDIA_PLAYER_PREVIOUS)
        previousIntent.putExtra("Previous", true)
        previousIntent.putExtra("Play_Pause", mediaplayer?.isPlaying)
        previousIntent.putExtra("Next", false)
        val previousPendingIntent = PendingIntent.getService(this, REQUEST_CODE, previousIntent,PendingIntent.FLAG_CANCEL_CURRENT)


        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_music)
            setContentTitle(currentSong.name)
            setContentText(currentSong.artist)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            addAction(
                    R.drawable.ic_previous,
                    "Previous",
                    previousPendingIntent
            )
            addAction(
                    R.drawable.ic_play,
                    "Play/Pause",
                    pendingIntent
            )
            addAction(
                    R.drawable.ic_next,
                    "Next",
                    nextPendingIntent
            )


        }.build()


        return notification
    }




}