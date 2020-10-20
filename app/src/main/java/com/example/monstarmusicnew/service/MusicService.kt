package com.example.monstarmusicnew.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.view.fragment.OfflineFragment
import com.example.monstarmusicnew.model.SongOffline
import com.example.monstarmusicnew.model.SongSearchOnline
import com.example.monstarmusicnew.viewmodel.MusicViewModel

class MusicService :Service(){
    var mu = MutableLiveData<SongOffline>()
    var musicFromService = MutableLiveData<SongOffline>()
    var cu= MutableLiveData<Int>()
    companion object {
        const val ACTION_PLAY = "play"
        const val ACTION_PREVIOUS = "previous"
        const val ACTION_NEXT = "next"
        const val ACTION_CLOSE = "close"
    }

    // var mNotificationManager :NotificationManager?=null
    //getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var mMusicViewModel: MusicViewModel
    private var mMusicManager: MusicManager? = null
    fun getMusicManager() = mMusicManager
    fun getModel() = mMusicViewModel

    override fun onCreate() {
        super.onCreate()
        mMusicViewModel = MusicViewModel()
        mMusicManager = MusicManager()
        registerChanel()

    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("stt", "inbinder")
        return MyBinder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("st", "stcm")
        return Service.START_REDELIVER_INTENT
    }
    fun searchSong(name: String) {
        mMusicViewModel.searchSong(name)
    }
    fun getFullLinkSong(position: Int) {

        mMusicViewModel.listMusicOnline.value?.let {
            mMusicViewModel.getFullLinkOnline(it[position].linkMusic)
        }
        //mMusicViewModel.getFullLinkOnline(mMusicViewModel.listMusicOnline.value!![position].linkMusic!!)
    }

    fun play(item: SongSearchOnline) {
        item.linkSong?.let {
            mMusicManager?.setData(this, it)
            mMusicManager?.play()
            //createNotificationMusic()
        }

    }

    fun playMusic(item: SongOffline) {
        Log.d("pl","pl")
        mMusicManager?.setData(this, item.uri)
        cu.value=mMusicManager?.mMediaPlayer?.currentPosition
        createNotificationMusic(item)
    }

    fun pauseMusic(item: SongOffline) {
        mMusicManager?.pause()

    }


    fun continuePlayMusic(item: SongOffline) {
        mMusicManager?.continuePlay()
    }

    fun stopMusic(item: SongOffline) {
        mMusicManager?.stop()

    }



    class MyBinder : Binder {
        var getService: MusicService

        constructor(musicService: MusicService) {
            this.getService = musicService
        }
    }

    private fun registerChanel() {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MusicService",
                "MusicService",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)

        }
    }

    private fun createNotificationMusic(item: SongOffline) {
        Log.d("no", item.toString())
        musicFromService.value = item
        //chỗ này log ra nó đã nhận bài hát đây rồi
        val notification = NotificationCompat.Builder(
            this,
            "MusicService"
        )
        val intent = Intent(this, OfflineFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("re", "okbalo")
        val intentContentActivity = PendingIntent.getActivity(this, 1, intent, 0)
        val intentBroadPlay = Intent().setAction(ACTION_PLAY)
        val actionIntentPlay =
            PendingIntent.getBroadcast(this, 0, intentBroadPlay, PendingIntent.FLAG_UPDATE_CURRENT)
        val intentBroadPrevious = Intent().setAction(ACTION_PREVIOUS)
        val actionIntentPrevious = PendingIntent.getBroadcast(
            this,
            0,
            intentBroadPrevious,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val intentBroadNext = Intent().setAction(ACTION_NEXT)
        val actionIntentNext =
            PendingIntent.getBroadcast(this, 0, intentBroadNext, PendingIntent.FLAG_UPDATE_CURRENT)
        val intentBroadClose = Intent().setAction(ACTION_CLOSE)
        val actionIntentClose =
            PendingIntent.getBroadcast(this, 0, intentBroadClose, PendingIntent.FLAG_UPDATE_CURRENT)
        notification.addAction(R.drawable.ic_baseline_skip_previous_24, "previous", actionIntentPrevious)
        notification.addAction(R.drawable.ic_baseline_pause_24, "play", actionIntentPlay)
        notification.addAction(R.drawable.ic_baseline_skip_next_24, "next", actionIntentNext)
        notification.addAction(R.drawable.ic_baseline_close_24, "Close", actionIntentClose)
        notification.setContentIntent(intentContentActivity)
        notification.setContentTitle("Music Offline From Phúc")
        notification.setContentText(item.nameMusic)
        notification.setSmallIcon(R.drawable.ic_baseline_library_music_24)
        notification.setAutoCancel(true)
        notification.priority = NotificationCompat.PRIORITY_LOW
        notification.setStyle(androidx.media.app.NotificationCompat.MediaStyle())
        notification.setLargeIcon(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.musicnew
            )
        )
        startForeground(10, notification.build())

    }

    override fun onDestroy() {
        Log.d("si", "ondestroy")
        super.onDestroy()
        mMusicManager?.stop()
        mMusicManager?.release()

    }

}