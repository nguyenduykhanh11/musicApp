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
import com.example.listnhac.databinding.FragmentAlbumBinding
import com.example.listnhac.model.entity.AlbumItem
import com.example.listnhac.viewModel.AlbumViewModel

class AlbumFragment: Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private lateinit var mAlbumViewModel: AlbumViewModel
    private lateinit var mAlbumAdapter: AlbumAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAlbumViewModel = ViewModelProvider(this)[AlbumViewModel::class.java]
        mAlbumAdapter = AlbumAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpView()
        getDataAlbumAPI()
        setUpClickItemMusicAlbum()
    }

//    Hàm click Album and send data to ListAlbumFragment
    private fun setUpClickItemMusicAlbum() {
        mAlbumAdapter.onClickItemAlbum = { album ->
            val navGraph = Navigation.findNavController(requireActivity(), R.id.host_fragment)
            val bundle = Bundle()
            bundle.putString("Album_music_id", album.id)
            bundle.putString("Album_music_name", album.albumName)
            bundle.putString("Album_music_singer", album.singerName)
            bundle.putString("Album_music_thumbnail", album.thumbnailName)
            navGraph.navigate(R.id.musicAlbumFragment, bundle)
        }
    }

//    Hàm getData từ Api
    private fun getDataAlbumAPI() {
        mAlbumViewModel.getAlbum()
        mAlbumViewModel.observeAlbum().observe(viewLifecycleOwner, {
            mAlbumAdapter.setAlbum(it as ArrayList<AlbumItem>)
        })
    }

//    hàm setUp ui RecyclerView
    private fun setUpView() {
        binding.recyclerViewFragmentAlbum.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAlbumAdapter
        }
    }
}