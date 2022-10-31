package com.example.listnhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listnhac.databinding.SongItemBinding
import com.example.listnhac.model.entity.SongListItem


class SongAdapter: RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    lateinit var onClickItem: ((SongListItem) ->Unit)
    var songList = ArrayList<SongListItem>()

    class ViewHolder(var binding: SongItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SongItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setUpView(holder, position)
        setUpListenerClickMusic(holder, position)
    }

    private fun setUpView(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(songList[position].thumbnail)
                .into(imgSongThumbnail)
            tvSinger.text = songList[position].singer
            tvSongTitle.text = songList[position].name
        }
    }

    private fun setUpListenerClickMusic(holder: ViewHolder, position: Int) {
        holder.binding.containerLayout.setOnClickListener {
            onClickItem.invoke(songList[position])
        }
    }

    override fun getItemCount(): Int = songList.size
    fun setSong(songList: ArrayList<SongListItem>){
        this.songList = songList
        notifyDataSetChanged()
    }
}
