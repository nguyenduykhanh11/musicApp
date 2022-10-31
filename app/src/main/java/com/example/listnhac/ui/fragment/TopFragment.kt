package com.example.listnhac.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.listnhac.databinding.FragmentIndividualBinding
import com.example.listnhac.databinding.FragmentTopBinding

class TopFragment: Fragment() {
    private lateinit var binding: FragmentTopBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopBinding.inflate(inflater,container,false)
        return binding.root
    }
}