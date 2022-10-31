package com.example.listnhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listnhac.databinding.SongItemBinding
import com.example.listnhac.model.entity.SongListItem

class ListMusicAlbumAdapter: RecyclerView.Adapter<ListMusicAlbumAdapter.ViewHolder>() {
    lateinit var onClickItem: ((SongListItem) ->Unit)
    var listMusicAlbum = ArrayList<SongListItem>()
    class ViewHolder(var binding: SongItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SongItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(listMusicAlbum[position].thumbnail)
                .into(imgSongThumbnail)
            tvSinger.text = listMusicAlbum[position].singer
            tvSongTitle.text = listMusicAlbum[position].name
        }
        setUpListenerClickMusic(holder, position)
    }

    private fun setUpListenerClickMusic(holder: ListMusicAlbumAdapter.ViewHolder, position: Int) {
        holder.binding.containerLayout.setOnClickListener {
            onClickItem.invoke(listMusicAlbum[position])
        }
    }

    override fun getItemCount(): Int = listMusicAlbum.size

    fun setSong(songList: ArrayList<SongListItem>){
        this.listMusicAlbum = songList
        notifyDataSetChanged()
    }

}