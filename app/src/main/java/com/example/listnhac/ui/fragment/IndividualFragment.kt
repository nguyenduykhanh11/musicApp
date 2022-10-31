package com.example.listnhac.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.listnhac.databinding.FragmentIndividualBinding

class IndividualFragment: Fragment() {
    private lateinit var binding: FragmentIndividualBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIndividualBinding.inflate(inflater,container,false)
        return binding.root
    }
}