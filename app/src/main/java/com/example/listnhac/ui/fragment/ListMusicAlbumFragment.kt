package com.example.listnhac.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.listnhac.MyServiceMusic
import com.example.listnhac.R
import com.example.listnhac.adapter.ListMusicAlbumAdapter
import com.example.listnhac.adapter.SongAdapter
import com.example.listnhac.databinding.FragmentMusicAlbumBinding
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.ui.activities.MainActivity
import com.example.listnhac.util.Contact
import com.example.listnhac.viewModel.ListMusicAlbumViewModel
import com.example.listnhac.viewModel.SongViewModel
import com.example.listnhac.viewModel.ViewModelBottomSheet

class ListMusicAlbumFragment : Fragment() {
    private lateinit var binding: FragmentMusicAlbumBinding
    private lateinit var mListMusicAlbumAdapter: ListMusicAlbumAdapter
    private lateinit var mListMusicAlbumViewModel: ListMusicAlbumViewModel
    private lateinit var mMainActivity: MainActivity
    private val mViewModelBottomSheet: ViewModelBottomSheet by activityViewModels()
    private var id: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListMusicAlbumViewModel = ViewModelProvider(this)[ListMusicAlbumViewModel::class.java]
        mListMusicAlbumAdapter = ListMusicAlbumAdapter()
        mMainActivity = MainActivity()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicAlbumBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpListenerBack()
        setUpViewRecyclerView()
        getDataMusicAlbumAPI()
        setUpOnClickItemSong()
    }

//    Hàm lấy data từ Api
    private fun getDataMusicAlbumAPI() {
        mListMusicAlbumViewModel.getListMusicAlbum(id.toString())
        mListMusicAlbumViewModel.obseverMusicAlbum().observe(viewLifecycleOwner) { musicAlbum ->
            mListMusicAlbumAdapter.setSong(musicAlbum as ArrayList<SongListItem>)
        }
    }

    private fun setUpViewRecyclerView() {
        binding.recyclerViewFragmentMusicAlbum.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mListMusicAlbumAdapter
        }
    }

    private fun setUpListenerBack() {
        binding.toolbar.setNavigationOnClickListener {
            val navGraph = Navigation.findNavController(requireActivity(), R.id.host_fragment)
            navGraph.popBackStack()
        }
    }

//    get data from AlbumFragment to setUpView
    private fun setUpView() {
        id = arguments?.getString("Album_music_id")
        val nameAlbum = arguments?.getString("Album_music_name")
        val nameSinger = arguments?.getString("Album_music_singer")
        val thumbnail = arguments?.getString("Album_music_thumbnail")
        with((activity as AppCompatActivity)){
            setSupportActionBar(binding.toolbar)
            title = nameAlbum
        }
        Glide.with(requireActivity())
            .load(thumbnail)
            .into(binding.imgTitleAppbar)
    }
//    Hàm xữ lý sự kiện click item adapter and lưu list music
    private fun setUpOnClickItemSong() {
        mListMusicAlbumAdapter.onClickItem={ song->
            val serviceIntent = Intent(context, MyServiceMusic::class.java)
            serviceIntent.putExtra(Contact.SONG, song)
            mListMusicAlbumViewModel.obseverMusicAlbum().observe(viewLifecycleOwner) {
                mViewModelBottomSheet.selectItem(it as ArrayList<SongListItem>)
            }
            ContextCompat.startForegroundService(requireContext(), serviceIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_music_album, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}