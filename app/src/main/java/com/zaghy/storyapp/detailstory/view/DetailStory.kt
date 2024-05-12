package com.zaghy.storyapp.detailstory.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.zaghy.storyapp.R
import com.zaghy.storyapp.databinding.FragmentDetailStoryBinding
import com.zaghy.storyapp.detailstory.viewmodel.DetailStoryViewModel
import com.zaghy.storyapp.detailstory.viewmodel.DetailStoryViewModelFactory
import com.zaghy.storyapp.network.Result


class DetailStory : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding
    private lateinit var idStory:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailStoryBinding.inflate(layoutInflater, container, false)
        val viewModel:DetailStoryViewModel by viewModels<DetailStoryViewModel> {
            DetailStoryViewModelFactory.getInstance(requireContext())
        }
        idStory = DetailStoryArgs.fromBundle(arguments as Bundle).id ?: ""
        viewModel.getDetailStory(idStory).observe(viewLifecycleOwner) {result->
            when(result){
                is Result.Loading->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success->{
                    binding.progressBar.visibility = View.GONE
                    Glide.with(binding.root)
                        .load(result.data.story?.photoUrl)
                        .into(binding.ivDetailPhoto)
                    binding.tvDetailName.text = result.data.story?.name
                    binding.tvDetailDescription.text = result.data.story?.description
                }
                is Result.Error->{
                    binding.progressBar.visibility = View.GONE
                }
                null ->{
//                    do nothing
                }
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val TAG = "detail"
    }
}