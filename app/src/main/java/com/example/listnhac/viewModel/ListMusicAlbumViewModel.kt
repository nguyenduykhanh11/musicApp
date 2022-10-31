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

class ListMusicAlbumViewModel: ViewModel() {
    private var listMusicAlbum = MutableLiveData<List<SongListItem>>()
    fun getListMusicAlbum(idAlbum: String){
        RetrofitInstance.api.getListMusicAlbum(idAlbum).enqueue(object : Callback<SongList>{
            override fun onResponse(call: Call<SongList>, response: Response<SongList>) {
                response.body()?.let {
                    listMusicAlbum.postValue(it)
                }
            }

            override fun onFailure(call: Call<SongList>, t: Throwable) {
                Log.d("Tag", "thất bại")
            }

        })
    }
    fun obseverMusicAlbum(): LiveData<List<SongListItem>>{
        return listMusicAlbum
    }
}