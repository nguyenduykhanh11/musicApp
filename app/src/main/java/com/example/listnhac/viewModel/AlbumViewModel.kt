package com.example.listnhac.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listnhac.model.entity.AlbumItem
import com.example.listnhac.model.entity.AlbumList
import com.example.listnhac.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumViewModel: ViewModel() {
    private var albumLiveData= MutableLiveData<List<AlbumItem>>()
    fun getAlbum(){
        RetrofitInstance.api.getAlbum().enqueue(object : Callback<AlbumList>{
            override fun onResponse(call: Call<AlbumList>, response: Response<AlbumList>) {
                response.body()?.let {
                    albumLiveData.postValue(it)
                }
            }
            override fun onFailure(call: Call<AlbumList>, t: Throwable) {
                Log.d("Tag", "thất bại")
            }
        })
    }

    fun observeAlbum(): LiveData<List<AlbumItem>>{
        return albumLiveData
    }
}