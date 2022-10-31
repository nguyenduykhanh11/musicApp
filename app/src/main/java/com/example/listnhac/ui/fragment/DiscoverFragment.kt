package com.example.listnhac.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listnhac.R
import com.example.listnhac.adapter.AlbumAdapter
import com.example.listnhac.adapter.CategoryAdapter
import com.example.listnhac.adapter.MyViewPegerAdapter
import com.example.listnhac.databinding.FragmentDiscoverBinding
import com.example.listnhac.model.entity.CategoryListItem
import com.example.listnhac.viewModel.CategoryViewModel
import com.example.listnhac.viewModel.ViewModelBottomSheet
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverFragment: Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var mCategoryViewModel: CategoryViewModel
    private lateinit var mAdapterPeger: MyViewPegerAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiscoverBinding.inflate(inflater,container,false)
        mCategoryAdapter = CategoryAdapter()
        mCategoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabLayout()
        setUpCategory()
        setUpView()
        setUpOnclickItemCategory()

    }

//    Hàm xữ lý sự kiện click thể loại and send data to MusicCategoryFragment
    private fun setUpOnclickItemCategory() {
        mCategoryAdapter.onClickItemCategory = { categoryItem ->
            val navGraph = Navigation.findNavController(requireActivity(), R.id.host_fragment)
            val bundle = Bundle()
            bundle.putString("Category_music_id", categoryItem.id)
            bundle.putString("Category_music_name", categoryItem.name)
            bundle.putString("Category_music_thumbnail", categoryItem.thumbnail)
            navGraph.navigate(R.id.musicCategoryFragment, bundle)
        }
    }

    private fun setUpView() {
        mCategoryViewModel.getCategory()
        mCategoryViewModel.observeCategory().observe(viewLifecycleOwner,{ category ->
            mCategoryAdapter.setCategory(category as ArrayList<CategoryListItem>)
        })
    }

    private fun setUpCategory() {
        binding.recyclerViewCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = mCategoryAdapter
        }
    }

    private fun setUpTabLayout() {
        mAdapterPeger = MyViewPegerAdapter(fragmentManager = childFragmentManager, lifecycle)
        binding.fragmentView.adapter = mAdapterPeger
        TabLayoutMediator(binding.tabLayoutMusicOrAlbum, binding.fragmentView){tab, position->
            when(position){
                0->{
                    tab.text = "Song"
                }
                1->{
                    tab.text = "Album"
                }
            }
        }.attach()
    }
}