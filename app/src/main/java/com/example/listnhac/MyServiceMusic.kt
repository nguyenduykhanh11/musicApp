package com.example.listnhac

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.util.Contact
import com.example.listnhac.util.Contact.SONG
import com.example.listnhac.viewModel.ViewModelBottomSheet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyServiceMusic: Service(), LifecycleOwner{
    private val CHANNEL_ID = "Notification from Service"

    var mediaPlayer: MediaPlayer? = null
    private var mListSongMusic: MutableList<SongListItem>? = mutableListOf()
    private lateinit var mViewModelBottomSheet: ViewModelBottomSheet
    private val mDispatcher = ServiceLifecycleDispatcher(this)
    private lateinit var mediaSession: MediaSessionCompat
    private var isPaying:Boolean? = false
    private var playBackSpeed = 1F
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null,null)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
            }
            mViewModelBottomSheet = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(ViewModelBottomSheet::class.java)

        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getSongfromFragmentSong(intent)
        listenerActionRecever(intent)
        listenerActionActivity(intent)
        return START_NOT_STICKY
    }

    private fun listenerActionActivity(intent: Intent?) {
        val action = intent?.getIntExtra(Contact.ACTION_ACTIVITY,7)
        if (action != null){
            actionMusic(action)
        }

    }

//    Hàm getData từ MainActivity
    private fun getSongfromFragmentSong(intent: Intent?) {
        val checkNull: SongListItem? = intent?.getParcelableExtra(SONG)
        if (checkNull != null){
            if(mListSongMusic!=null){
                mediaPlayer?.release()
                mediaPlayer = null
                mListSongMusic?.clear()
            }
            mListSongMusic?.add(checkNull)
            playingMusic(mListSongMusic!![0])
        }
    }

//    Hàm sữ lý việc Start Music
    private fun playingMusic(songMusic: SongListItem?) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setDataSource(songMusic?.link)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start()
            notificationForeground(songMusic)
//            mViewModelBottomSheet.let(lifecycle::addObserver)
//            mViewModelBottomSheet.apply {
//                ducaItem(mediaPlayer.duration.toLong())
//            }

//            Xữ lý sự kiện next khi music chạy hêt
            mediaPlayer.setOnCompletionListener {
                actionMusic(Contact.ACTION_NEXT)
            }
        }
        isPaying =true
        sendActionToActivity(Contact.ACTION_START)
    }

//    Hàm Lắng nghe Action được nhận từ Broadcast Recever
    private fun listenerActionRecever(intent: Intent?) {
        val actionMusicReceiver = intent?.getStringExtra("action_music_service")
        if (actionMusicReceiver != null) {
            actionMusic(actionMusicReceiver.toInt())
        }
    }

//    Hàm xữ lý các sự kiện Action
    private fun actionMusic(action: Int) {
        when (action) {
            Contact.ACTION_PAUSE -> {
                pauseMusic()
            }
            Contact.ACTION_RESUM -> {
                resumMusic()
            }
            Contact.ACTION_BACK -> {
                sendActionToActivity(Contact.ACTION_BACK)

            }
            Contact.ACTION_NEXT -> {
                sendActionToActivity(Contact.ACTION_NEXT)
            }
            Contact.ACTION_CLOSE -> {
                stopSelf()
                sendActionToActivity(Contact.ACTION_CLOSE)
            }
        }
    }

//    Hàm resum nhạc
    private fun resumMusic() {
        if (isPaying==false){
            mediaPlayer!!.start()
            isPaying = true
            notificationForeground(mListSongMusic!![0])
            sendActionToActivity(Contact.ACTION_RESUM)
        }
    }

//    Hàm Pause nhạc
    private fun pauseMusic() {
        if (isPaying==true){
            mediaPlayer!!.pause()
            isPaying = false
            notificationForeground(mListSongMusic!![0])
            sendActionToActivity(Contact.ACTION_PAUSE)
        }
    }

//    Hàm load ảnh bất đồng bộ
    private suspend fun getBitmap(): Bitmap{
        val loading = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(mListSongMusic!![0].thumbnail)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

//    Hàm sữ lý sự kiện Media for notification
    private fun mediaSesion() {
        mediaSession = MediaSessionCompat(this, "TAG")
        if (isPaying == true) playBackSpeed = 1F else playBackSpeed = 0F
        mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(),playBackSpeed)
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            .build())
        mediaSession.setMetadata(MediaMetadataCompat.Builder()
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
            .build())
    }

//    Hàm send notification for foreBackground
    private fun notificationForeground(songMusic: SongListItem?) {
        mediaSesion()
        val NotificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_notification_music)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.sessionToken))
            .setSound(null)
            .setContentTitle(songMusic?.name)
            .setContentText(songMusic?.singer)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_back, "back", getPendingIntent(this, Contact.ACTION_BACK))
        if (isPaying==true){
            NotificationBuilder
                .addAction(R.drawable.ic_pause, "Pause", getPendingIntent(this, Contact.ACTION_PAUSE))
        }else{
            NotificationBuilder
                .addAction(R.drawable.ic_play, "Pause", getPendingIntent(this, Contact.ACTION_RESUM))
        }
            .addAction(R.drawable.ic_next, "Next", getPendingIntent(this, Contact.ACTION_NEXT))
            .addAction(R.drawable.ic_close, "Close", getPendingIntent(this, Contact.ACTION_CLOSE))

        GlobalScope.launch {
            val bitmap = getBitmap()
            val notification = NotificationBuilder
                .setLargeIcon(bitmap)
                .build()
            startForeground(1, notification)
        }
    }

//    Hàm nhận sự kiện click item in notification
    private fun getPendingIntent(context: MyServiceMusic, action: Int): PendingIntent {
        val intent = Intent(this,MyReceverMusic::class.java)
        intent.putExtra("action_music", action.toString())
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

//    Hàm send data to MainActiviy
    private fun sendActionToActivity(action: Int){
        val intent = Intent("Send_Data_to_Activity").apply {
            putExtra("Activity_song", mListSongMusic!![0])
            putExtra("Status_song", isPaying)
            putExtra("Action_music", action)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release()
        mediaPlayer
    }

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }
}