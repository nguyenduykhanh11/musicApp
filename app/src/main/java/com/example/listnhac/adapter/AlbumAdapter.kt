package com.example.listnhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listnhac.databinding.AlbumItemBinding
import com.example.listnhac.model.entity.AlbumItem

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    lateinit var onClickItemAlbum: ((AlbumItem) ->Unit)
    private var albumList = ArrayList<AlbumItem>()
    class ViewHolder(var binding: AlbumItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(albumList[position].thumbnailName)
                .into(imgAlbumThumbnail)
            tvAlbumSinger.text = albumList[position].singerName
            tvAlbumTitle.text = albumList[position].albumName
        }
        setUpListenerClickMusicAlbum(holder, position)
    }

    override fun getItemCount(): Int = albumList.size

    private fun setUpListenerClickMusicAlbum(holder: ViewHolder, position: Int) {
        holder.binding.containerLayout.setOnClickListener {
            onClickItemAlbum.invoke(albumList[position])
        }
    }

    fun setAlbum(albumList: ArrayList<AlbumItem>){
        this.albumList = albumList
        notifyDataSetChanged()
    }
}