package com.example.listnhac.model.entity

import android.os.Parcel
import android.os.Parcelable

data class SongListItem(
    val id: String?,
    val link: String?,
    val name: String?,
    val singer: String?,
    val thumbnail: String?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(link)
        parcel.writeString(name)
        parcel.writeString(singer)
        parcel.writeString(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongListItem> {
        override fun createFromParcel(parcel: Parcel): SongListItem {
            return SongListItem(parcel)
        }

        override fun newArray(size: Int): Array<SongListItem?> {
            return arrayOfNulls(size)
        }
    }
}

