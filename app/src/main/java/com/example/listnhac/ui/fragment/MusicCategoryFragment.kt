package com.example.listnhac.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.listnhac.MyServiceMusic
import com.example.listnhac.R
import com.example.listnhac.adapter.ListMusicCategoryAdapter
import com.example.listnhac.databinding.FragmentMusicCategoryBinding
import com.example.listnhac.model.entity.SongListItem
import com.example.listnhac.util.Contact
import com.example.listnhac.viewModel.ListMusicCategoryViewModel
import com.example.listnhac.viewModel.ViewModelBottomSheet

class MusicCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMusicCategoryBinding
    private lateinit var mListMusicCategoryViewModel: ListMusicCategoryViewModel
    private lateinit var mListMusicCategoryAdapter: ListMusicCategoryAdapter
    private val mViewModelBottomSheet: ViewModelBottomSheet by activityViewModels()

    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListMusicCategoryViewModel = ViewModelProvider(this)[ListMusicCategoryViewModel::class.java]
        mListMusicCategoryAdapter = ListMusicCategoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpListenerBack()
        setUpViewRecyclerView()
        getDataMusicCategoryAPI()
        setUpOnClickItemSong()
    }

//    Hàm xữ lý click item adapter
    private fun setUpOnClickItemSong() {
        mListMusicCategoryAdapter.onClickItemCategory={ song->
            val serviceIntent = Intent(context, MyServiceMusic::class.java)
            serviceIntent.putExtra(Contact.SONG, song)
            mListMusicCategoryViewModel.obseverMusicCategory().observe(viewLifecycleOwner) {
                mViewModelBottomSheet.selectItem(it as ArrayList<SongListItem>)
            }
            ContextCompat.startForegroundService(requireContext(), serviceIntent)
        }
    }

//    function get data form api
    private fun getDataMusicCategoryAPI() {
        mListMusicCategoryViewModel.getListMusicCategory(id.toString())
        mListMusicCategoryViewModel.obseverMusicCategory().observe(viewLifecycleOwner,{ MusicCategory ->
            mListMusicCategoryAdapter.setListMusicCategory(MusicCategory)
        })
    }

//    hàm setUp View recyclerView
    private fun setUpViewRecyclerView() {
        binding.recyclerViewFragmentMusicCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mListMusicCategoryAdapter
        }
    }

//    Hàm get data and set up view
    private fun setUpView() {
        id = arguments?.getString("Category_music_id")
        Log.d("test", id.toString())
        val nameAlbum = arguments?.getString("Category_music_name")
        val thumbnail = arguments?.getString("Category_music_thumbnail")
        with((activity as AppCompatActivity)){
            setSupportActionBar(binding.toolbar)
            title = nameAlbum
        }
        Glide.with(requireActivity())
            .load(thumbnail)
            .into(binding.imgTitleAppbar)
    }

//    Hàm xữ lý sự kiện click back
    private fun setUpListenerBack() {
        binding.toolbar.setNavigationOnClickListener {
            val navGraph = Navigation.findNavController(requireActivity(), R.id.host_fragment)
            navGraph.popBackStack()
        }
    }

}