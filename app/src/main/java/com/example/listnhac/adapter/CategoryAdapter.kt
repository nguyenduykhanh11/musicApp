package com.example.listnhac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listnhac.databinding.AlbumItemBinding
import com.example.listnhac.databinding.CategoryItemBinding
import com.example.listnhac.model.entity.AlbumItem
import com.example.listnhac.model.entity.CategoryListItem

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var categoryList = ArrayList<CategoryListItem>()
    lateinit var onClickItemCategory: ((CategoryListItem) ->Unit)
    class ViewHolder(var binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(categoryList[position].thumbnail)
                .into(imgCategory)
        }
        setUpListenerClickCategoryItem(holder, position)
    }

    private fun setUpListenerClickCategoryItem(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.binding.categoryLayout.setOnClickListener {
            onClickItemCategory.invoke(categoryList[position])
        }
    }

    override fun getItemCount(): Int = categoryList.size

    fun setCategory(categoryList: ArrayList<CategoryListItem>){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}