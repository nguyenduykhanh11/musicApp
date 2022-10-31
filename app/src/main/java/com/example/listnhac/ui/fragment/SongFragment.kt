package com.example.listnhac.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listnhac.MyServiceMusic
import com.example.listnhac.R
import com.example.listnhac.adapter.SongAdapter
import com.example.listnhac.databinding.FragmentSongBinding
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.ui.activities.MainActivity
import com.example.listnhac.util.Contact.SONG
import com.example.listnhac.viewModel.SongViewModel
import com.example.listnhac.viewModel.ViewModelBottomSheet
import kotlin.math.log

class SongFragment: Fragment() {
    private lateinit var binding: FragmentSongBinding
    private lateinit var mSongViewModel: SongViewModel
    private lateinit var mSongAdapter: SongAdapter
    private lateinit var mMainActivity: MainActivity
    private val mViewModelBottomSheet: ViewModelBottomSheet by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSongViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        mSongAdapter = SongAdapter()
        mMainActivity = MainActivity()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        getDataSongAPI()
        setUpOnClickItemSong()
        setUpListenerSeeMore()
    }

//    Hàm xữ lý click xem thêm
    private fun setUpListenerSeeMore() {
        binding.tvXemThem.setOnClickListener {
            val navGraph = Navigation.findNavController(requireActivity(), R.id.host_fragment)
            navGraph.navigate(R.id.listMusicSongFragment)
        }
    }

//    hàm lấy data từ Api
    private fun getDataSongAPI() {
        mSongViewModel.getSong()
        mSongViewModel.observeSong().observe(viewLifecycleOwner) {
            mSongAdapter.setSong(it as ArrayList<SongListItem>)

        }
    }

//    setUpView cho SongAdapter
    private fun setUpView() {
        binding.recyclerViewFragmentMusic.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mSongAdapter
        }
    }

//    Hàm xữ lý onClick Item adapter and lưu danh sách music
    private fun setUpOnClickItemSong() {
        mSongAdapter.onClickItem={ song->
            val serviceIntent = Intent(context, MyServiceMusic::class.java)
            serviceIntent.putExtra(SONG, song)
            mSongViewModel.observeSong().observe(viewLifecycleOwner) {
                mViewModelBottomSheet.selectItem(it as ArrayList<SongListItem>)
            }
            ContextCompat.startForegroundService(requireContext(), serviceIntent)
        }
    }
}