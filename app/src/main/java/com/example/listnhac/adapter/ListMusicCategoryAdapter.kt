package com.example.listnhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listnhac.databinding.CategoryMusicItemBinding
import com.example.listnhac.databinding.SongItemBinding
import com.example.listnhac.model.entity.SongListItem

class ListMusicCategoryAdapter: RecyclerView.Adapter<ListMusicCategoryAdapter.ViewHolder> (){
    lateinit var onClickItemCategory: ((SongListItem) ->Unit)
    var listMusicCategory = ArrayList<SongListItem>()
    class ViewHolder(var binding: CategoryMusicItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryMusicItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(listMusicCategory[position].thumbnail)
                .into(imgSongThumbnail)
            tvSinger.text = listMusicCategory[position].singer
            tvSongTitle.text = listMusicCategory[position].name
        }
        setUpListenerClickMusic(holder, position)
    }

    private fun setUpListenerClickMusic(holder: ListMusicCategoryAdapter.ViewHolder, position: Int) {
        holder.binding.containerLayout.setOnClickListener {
            onClickItemCategory.invoke(listMusicCategory[position])
        }
    }

    override fun getItemCount(): Int = listMusicCategory.size

    fun setListMusicCategory(listMusicCategory: List<SongListItem>){
        this.listMusicCategory = listMusicCategory as ArrayList<SongListItem>
        notifyDataSetChanged()
    }
}