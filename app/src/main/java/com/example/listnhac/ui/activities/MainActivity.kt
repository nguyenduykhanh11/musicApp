package com.example.listnhac.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.listnhac.MyServiceMusic
import com.example.listnhac.R
import com.example.listnhac.databinding.ActivityMainBinding
import com.example.listnhac.model.entity.AlbumItem
import com.example.listnhac.model.entity.SongList
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.ui.fragment.*
import com.example.listnhac.util.Contact
import com.example.listnhac.viewModel.ViewModelBottomSheet
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var songMusic: SongListItem
    private var listSong =  mutableListOf<SongListItem>()
    private var isPlaying: Boolean = false
    private val mViewModelBottomSheet: ViewModelBottomSheet by viewModels()

//    lấy dữ liệu từ mySevice
    private var broadcastReceiver= object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            songMusic = intent?.getParcelableExtra("Activity_song")!!
            isPlaying = intent.getBooleanExtra("Status_song", false)
            val action = intent.getIntExtra("Action_music", 7)
            handelActionMusic(action)
        }
    }

//    nhận sự kiện click các tác vụ click từ Service and fragment
    private fun handelActionMusic(action: Int) {
        when(action){
            Contact.ACTION_START->{
                binding.cdLayoutBottom.isVisible = true
                showInfoSong()
                setUpListener()
                setStatusButtomPlayOrPause()
            }
            Contact.ACTION_PAUSE->{
                sendActionToService(Contact.ACTION_PAUSE)
                setStatusButtomPlayOrPause()
            }
            Contact.ACTION_CLOSE->{
                binding.cdLayoutBottom.isVisible = false
            }
            Contact.ACTION_NEXT->{
                setNextMusicSong()
            }
            Contact.ACTION_BACK->{
                setStatusButtomPlayOrPause()
                setBackMusicSong()
            }
            Contact.ACTION_RESUM->{
                sendActionToService(Contact.ACTION_RESUM)
                setStatusButtomPlayOrPause()
            }
        }
    }
//    Hàm xữ lý sự kiện Back bài hát
    private fun setBackMusicSong() {
        for (i in 0 until listSong.size){
            if (listSong[i].id == songMusic.id){
                val serviceIntent = Intent(this, MyServiceMusic::class.java)
                if (i == 0){
                    serviceIntent.putExtra(Contact.SONG, listSong[listSong.size-1])
                }else{
                    serviceIntent.putExtra(Contact.SONG, listSong[i-1])
                }
                ContextCompat.startForegroundService(applicationContext, serviceIntent)
            }
        }
    }

//    Hàm xữ lý sự kiện Next bài hát
    private fun setNextMusicSong() {
        for (i in 0 until listSong.size){
            if (listSong[i].id == songMusic.id){
                val serviceIntent = Intent(this, MyServiceMusic::class.java)
                if (i == listSong.size-1){
                    serviceIntent.putExtra(Contact.SONG, listSong[0])
                }else{
                    serviceIntent.putExtra(Contact.SONG, listSong[i+1])
                }
                ContextCompat.startForegroundService(applicationContext, serviceIntent)
            }
        }
    }

//    Hàm viết sự kiện gửi thông tin pause or start and next từ MainActivity
    private fun setUpListener() {
        with(binding){
            imgPause.setOnClickListener {
                if(isPlaying) sendActionToService(Contact.ACTION_PAUSE) else sendActionToService(Contact.ACTION_RESUM)
                }
            imgNext.setOnClickListener {
                handelActionMusic(Contact.ACTION_NEXT)
            }
        }
    }

//    Hàm set Trạng thái layout pause or start
    private fun setStatusButtomPlayOrPause() {
        if (isPlaying){
            binding.imgPause.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
        }
        else{
            binding.imgPause.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
        }
    }

//    Hàm ui thông tin bài hát đang Play
    private fun showInfoSong() {
        if (songMusic == null)  return
        with(binding){
            Glide.with(this@MainActivity)
                .load(songMusic.thumbnail)
                .into(imgSongThumbnail)
            tvNameSong.text = songMusic.name
            tvNameSinger.text = songMusic.singer
        }
        sendDataViewModelSong()
    }

//    Hàm xữ lý sự kiện send data to MyService
    private fun sendActionToService(action: Int){
        val intentService = Intent(this@MainActivity, MyServiceMusic::class.java)
        intentService.putExtra(Contact.ACTION_ACTIVITY, action)
        startService(intentService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter("Send_Data_to_Activity"))
        setUpListenerBottomSheetMusic()
        getActionFromBottomSheet()
        setDataListSong()
    }

//    Hàm lưu list bài hát để cho việc thực hiện back or next
    private fun setDataListSong() {
        mViewModelBottomSheet.selectedItem.observe(this, Observer { item ->
            listSong.clear()
            item.forEach {
                listSong.add(it)
            }
        })
    }

//    Hàm lấy thông tin dc xữ lý từ boottomSheet
    private fun getActionFromBottomSheet() {
        mViewModelBottomSheet.actionMusic.observe(this){
            handelActionMusic(it)
        }
    }

//    Hàm setUpView and send data to bottomSheet
    private fun setUpListenerBottomSheetMusic() {
        binding.cdLayoutBottom.setOnClickListener{
            ModeBottomSheetMusic().show(supportFragmentManager, "SheetMusic")
        }
    }
//    Hàm lưu thông tin đến ViewModel
    private fun sendDataViewModelSong(){
        mViewModelBottomSheet.nameSong.value = songMusic.name
        mViewModelBottomSheet.nameSinger.value = songMusic.singer
        mViewModelBottomSheet.thumbnail.value = songMusic.thumbnail
        mViewModelBottomSheet.isPlaying.value = isPlaying
    }

    private fun setUpView() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }


}