package com.example.listnhac.model.entity

import java.io.Serializable

data class AlbumItem(
    val albumName: String,
    val id: String,
    val singerName: String,
    val thumbnailName: String
) : Serializable