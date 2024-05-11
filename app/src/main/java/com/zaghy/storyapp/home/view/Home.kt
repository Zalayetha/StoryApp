package com.zaghy.storyapp.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zaghy.storyapp.R
import com.zaghy.storyapp.adapter.RecyclerViewStoryAdapter
import com.zaghy.storyapp.databinding.FragmentHomeBinding
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.home.viewmodel.HomeViewModel
import com.zaghy.storyapp.home.viewmodel.HomeViewModelFactory
import com.zaghy.storyapp.network.Result

class Home : Fragment() {
    private lateinit var  binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val viewModel:HomeViewModel by viewModels<HomeViewModel> {
            HomeViewModelFactory.getInstance(requireContext())
        }
        viewModel.getStories().observe(viewLifecycleOwner){result->
            when(result){
                is Result.Loading->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.nodata.visibility = View.GONE
                }
                is Result.Success->{
                    binding.progressBar.visibility = View.GONE
                    binding.nodata.visibility = View.GONE

                    setStories(result.data.listStory)
                }
                is Result.Error->{
                    binding.progressBar.visibility = View.GONE
                    binding.nodata.visibility = View.VISIBLE
                }
                null->{
//                    do nothing
                }
            }
        }


        binding.storyRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }




        return binding.root
    }

    companion object {

        private const val TAG = "Home"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout ->{
                Log.e(TAG, "onOptionsItemSelected: Logout")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStories(story:List<ListStoryItem?>?){
        if((story?.size ?: 0) > 0){
            binding.storyRv.visibility = View.VISIBLE
            binding.nodata.visibility = View.GONE
            val adapter = RecyclerViewStoryAdapter<ListStoryItem>(
                diffCallback = object :DiffUtil.ItemCallback<ListStoryItem>(){
                    override fun areItemsTheSame(
                        oldItem: ListStoryItem,
                        newItem: ListStoryItem
                    ): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(
                        oldItem: ListStoryItem,
                        newItem: ListStoryItem
                    ): Boolean {
                        return oldItem == newItem
                    }

                },
                bindView = {item,binding->
                    Glide.with(binding.root).load(item.photoUrl).into(binding.ivItemPhoto)
                    binding.tvItemName.text = item.name
                    binding.tvDescription.text = item.description
                },
                onClick = {item->
                    view?.findNavController()?.navigate(R.id.action_homepage_to_detailStory)
                }

            )

            adapter.submitList(story)
            binding.storyRv.adapter = adapter
        }

    }
}