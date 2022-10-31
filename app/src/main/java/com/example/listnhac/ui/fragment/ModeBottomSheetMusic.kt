package com.example.listnhac.ui.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.listnhac.MyServiceMusic
import com.example.listnhac.R
import com.example.listnhac.databinding.CustomSheetMusicBinding
import com.example.listnhac.ui.activities.MainActivity
import com.example.listnhac.util.Contact
import com.example.listnhac.viewModel.ViewModelBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ModeBottomSheetMusic: BottomSheetDialogFragment() {
    private lateinit var binding: CustomSheetMusicBinding
    private lateinit var mDialog: BottomSheetDialog
    private lateinit var mMyServiceMusic: MyServiceMusic
    private val mViewModelBottomSheet: ViewModelBottomSheet by activityViewModels()
    private var isPlaying: Boolean = false

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMyServiceMusic = MyServiceMusic()
//        \mViewModelBottomSheet =  ViewModelProvider(this).get(ViewModelBottomSheet::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomSheetMusicBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpView()
        setUpHiddenDownView()
        setUpListenerPauseMusic()
        setUpSeekBar()
    }

    private fun setUpSeekBar() {
        binding.seekBarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if(fromUser) mMyServiceMusic.mediaPlayer?.seekTo(progress)
//                binding.tvNameSinger.text = progress.toString()
                //binding.seekBarMusic.progress = mMyServiceMusic.mediaPlayer!!.currentPosition
//                val handler = Handler()
//                handler.postDelayed(object : Runnable{
//                    override fun run() {
//                        try {
//                            binding.seekBarMusic.max = mMyServiceMusic.mediaPlayer!!.duration
//                            binding.seekBarMusic.progress = mMyServiceMusic.mediaPlayer?.currentPosition!!
//                            handler.postDelayed(this, 1000)
//                        }catch (e: Exception){
//                            binding.seekBarMusic.progress = 0
//                        }
//                    }
//                }, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })
    }

//    Hàm chứa các sự kiện onClick
    private fun setUpListenerPauseMusic() {
        val animFade = AnimationUtils.loadAnimation(requireContext(), R.anim.turn)
        binding.imgPauseOrPlay.setOnClickListener {
            if (isPlaying) {
                binding.imgPauseOrPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
                sendActionToActivity(Contact.ACTION_PAUSE)
                binding.imgActionMusic.clearAnimation()
                isPlaying = false
            }else {
                binding.imgPauseOrPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                sendActionToActivity(Contact.ACTION_RESUM)
                binding.imgActionMusic.startAnimation(animFade)
                isPlaying = true
            }
        }
        binding.imgBack.setOnClickListener {
            sendActionToActivity(Contact.ACTION_BACK)
        }
        binding.imgNext.setOnClickListener {
            sendActionToActivity(Contact.ACTION_NEXT)
        }

    }

//    hàm send Action to MainActivity
    private fun sendActionToActivity(action: Int){
        mViewModelBottomSheet.actionMusic.value = action
    }

//    Hàm hint botomSheet if click imgDown
    private fun setUpHiddenDownView() {
        binding.imgDown.setOnClickListener {
            mDialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

//    hàm show ui
    private fun setUpView() {
        mDialog = dialog as BottomSheetDialog
        mDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        showInfoSong()
    }

//    Hàm thiết lập View
    private fun showInfoSong() {
        with(mViewModelBottomSheet){
            nameSong.observe(viewLifecycleOwner) {
                binding.tvNameSong.text = String.format(it)
            }
            nameSinger.observe(viewLifecycleOwner) {
                binding.tvNameSinger.text = String.format(it)
            }
            thumbnail.observe(viewLifecycleOwner) {
                if (it != "") {
                    Glide.with(requireActivity())
                        .load(it)
                        .into(binding.imgActionMusic)
                }
            }
        }
        mViewModelBottomSheet.isPlaying.observe(viewLifecycleOwner) {
            isPlaying = it
            setActionPauseOrPlay()
        }
    }

//    hàm set status for img pause or play
    private fun setActionPauseOrPlay() {
        val animFade = AnimationUtils.loadAnimation(requireContext(), R.anim.turn)
        if (isPlaying){
            binding.imgPauseOrPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
            binding.imgActionMusic.startAnimation(animFade)
        }else{
            binding.imgPauseOrPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
            binding.imgActionMusic.clearAnimation()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}