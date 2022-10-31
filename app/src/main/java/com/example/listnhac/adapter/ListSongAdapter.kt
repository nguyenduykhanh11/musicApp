package com.example.listnhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listnhac.databinding.CategoryMusicItemBinding
import com.example.listnhac.databinding.SongItemBinding
import com.example.listnhac.model.entity.SongListItem

class ListSongAdapter: RecyclerView.Adapter<ListSongAdapter.ViewHolder>() {
    private var songList = ArrayList<SongListItem>()
    class ViewHolder(var binding: CategoryMusicItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryMusicItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(songList[position].thumbnail)
                .into(imgSongThumbnail)
            tvSinger.text = songList[position].singer
            tvSongTitle.text = songList[position].name
        }
    }

    override fun getItemCount(): Int = songList.size

    fun setListSong(songList: ArrayList<SongListItem>){
        this.songList = songList
        notifyDataSetChanged()
    }
}

