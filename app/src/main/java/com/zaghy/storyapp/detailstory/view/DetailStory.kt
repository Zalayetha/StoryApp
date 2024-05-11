package com.zaghy.storyapp.detailstory.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.zaghy.storyapp.R
import com.zaghy.storyapp.databinding.FragmentDetailStoryBinding


class DetailStory : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataPhoto = DetailStoryArgs.fromBundle(arguments as Bundle).photo
        val dataTitle = DetailStoryArgs.fromBundle(arguments as Bundle).title
        val dataDescription = DetailStoryArgs.fromBundle(arguments as Bundle).description

        Glide.with(binding.root)
            .load(dataPhoto)
            .into(binding.ivDetailPhoto)

        binding.tvDetailName.text = dataTitle
        binding.tvDetailDescription.text = dataDescription
    }

    companion object {
        private const val TAG = "detail"
    }
}