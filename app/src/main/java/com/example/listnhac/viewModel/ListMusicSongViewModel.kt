package com.example.listnhac.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listnhac.model.entity.SongList
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMusicSongViewModel: ViewModel() {
    private var listMusicSong = MutableLiveData<List<SongListItem>>()
    fun getListMusicSong(){
        RetrofitInstance.api.getListSong().enqueue(object : Callback<SongList> {
            override fun onResponse(call: Call<SongList>, response: Response<SongList>) {
                response.body()?.let {
                    listMusicSong.postValue(it)
                }
            }
            override fun onFailure(call: Call<SongList>, t: Throwable) {
                Log.d("Tag", "thất bại")
            }
        })
    }
    fun obseverMusicSong(): LiveData<List<SongListItem>> {
        return listMusicSong
    }
}