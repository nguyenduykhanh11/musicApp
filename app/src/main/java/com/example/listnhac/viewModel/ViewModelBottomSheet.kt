package com.example.listnhac.viewModel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listnhac.model.entity.SongListItem

class ViewModelBottomSheet: ViewModel() {
//    lưu thông tin bài hát
    var nameSong = MutableLiveData<String>()
    var nameSinger = MutableLiveData<String>()
    var thumbnail = MutableLiveData<String>()

//    lưu action and trạng thái music
    var actionMusic = MutableLiveData<Int>()
    var isPlaying = MutableLiveData<Boolean>()

//    danh sách nhạc back or next service
    private val mutableSelectedItem = MutableLiveData<ArrayList<SongListItem>>()
    val selectedItem: LiveData<ArrayList<SongListItem>> get() = mutableSelectedItem
    fun selectItem(listSong: ArrayList<SongListItem>) {
        mutableSelectedItem.postValue(listSong)
    }

    //    setTimeSeekbar
    private val mutableDucation = MutableLiveData<Long>()
    val DucationItem: LiveData<Long> get() = mutableDucation
    fun ducaItem(listSong: Long) {
        mutableDucation.postValue(listSong)
    }

}