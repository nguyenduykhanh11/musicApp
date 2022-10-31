package com.example.listnhac.retrofit

import com.example.listnhac.model.entity.AlbumList
import com.example.listnhac.model.entity.CategoryList
import com.example.listnhac.model.entity.SongList
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SongApi {
    @GET("baihat.php")
    fun getSong(): Call<SongList>

    @GET("listBaihat.php")
    fun getListSong(): Call<SongList>

    @GET("album.php")
    fun getAlbum(): Call<AlbumList>

    @GET("theloai.php")
    fun getCategory(): Call<CategoryList>

    @GET("danhsachnhac.php?")
    fun getListMusicAlbum(@Query("idAlbum") musicAlbum: String): Call<SongList>

    @GET("danhsachnhac.php?")
    fun getListMusicCategory(@Query("idCategory") musicCategory: String): Call<SongList>

}