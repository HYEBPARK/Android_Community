package com.cookandroid.mylife.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.cookandroid.mylife.R
import com.cookandroid.mylife.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false )

        binding.tipTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)

        }

        binding.homeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }

        binding.storeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }

        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }


        return binding.root
    }


}