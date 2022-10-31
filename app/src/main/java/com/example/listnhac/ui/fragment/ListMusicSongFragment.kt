package com.example.listnhac.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listnhac.R
import com.example.listnhac.adapter.ListSongAdapter
import com.example.listnhac.databinding.FragmentListMusicSongBinding
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.viewModel.ListMusicSongViewModel

class ListMusicSongFragment : Fragment() {
    private lateinit var binding: FragmentListMusicSongBinding
    private lateinit var mListMusicSongViewModel: ListMusicSongViewModel
    private lateinit var mListSongAdapter: ListSongAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListMusicSongViewModel = ViewModelProvider(this)[ListMusicSongViewModel::class.java]
        mListSongAdapter =ListSongAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListMusicSongBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewRecyclerView()
        setUpListenerBack()
        getDataMusicAlbumAPI()
    }

//    Hàm get data from api
    private fun getDataMusicAlbumAPI() {
        mListMusicSongViewModel.getListMusicSong()
        mListMusicSongViewModel.obseverMusicSong().observe(viewLifecycleOwner,{ MusicAlbum ->
            mListSongAdapter.setListSong(MusicAlbum as ArrayList<SongListItem>)
        })
    }

//    setUpView recyclerView
    private fun setUpViewRecyclerView() {
        binding.recyclerViewFragmentMusicSong.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mListSongAdapter
        }
    }

    private fun setUpListenerBack() {
        binding.toolbar.setNavigationOnClickListener {
            val navGraph = Navigation.findNavController(requireActivity(), R.id.host_fragment)
            navGraph.popBackStack()
        }
    }

}