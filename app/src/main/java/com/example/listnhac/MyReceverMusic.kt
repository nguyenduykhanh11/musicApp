package com.example.listnhac

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceverMusic: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        Nhận action từ sự kiện click Service and send action to Service
        val actionMusic = intent?.getStringExtra("action_music")
        val intentService = Intent(context, MyServiceMusic::class.java)
        intentService.putExtra("action_music_service", actionMusic)
        context?.startService(intentService)
    }

}