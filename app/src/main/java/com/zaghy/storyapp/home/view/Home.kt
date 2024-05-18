package com.zaghy.storyapp.home.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.zaghy.storyapp.widget.CustomAlertDialog

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.storyRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        setupAction()
        viewModel.getUser().observe(viewLifecycleOwner){

            viewModel.getStories(it.token).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        Log.d(TAG, "LOADING")
                        binding.progressBar.visibility = View.VISIBLE
                        binding.nodata.visibility = View.GONE
                    }

                    is Result.Success -> {
                        Log.d(TAG, "SUCCESS")
                        binding.progressBar.visibility = View.GONE
                        binding.nodata.visibility = View.GONE

                        setStories(result.data.listStory)
                    }

                    is Result.Error -> {
                        Log.d(TAG, "ERROR : ${result.error}")
                        binding.progressBar.visibility = View.GONE
                        binding.nodata.visibility = View.VISIBLE
                    }

                    null -> {
//                    do nothing
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        // Ensure that the parent activity is an AppCompatActivity
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.myToolbar)

        // Optional: Set title
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Story App"

    }


    private fun setupAction() {
        binding.btnAddStory.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homepage_to_addStory)
        }
        viewModel.navigateToLoginPage.observe(viewLifecycleOwner){shouldNavigate->
            if (shouldNavigate){

                view?.findNavController()?.navigate(R.id.action_homepage_to_welcome)
            }

        }
    }

    companion object {

        private const val TAG = "Home"
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
               val builder = AlertDialog.Builder(requireContext())
                builder
                    .setTitle("Informasi")
                    .setMessage("Apakah anda yakin ingin logout?")
                    .setPositiveButton("Yakin"){dialog,_->
                        viewModel.logout()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal"){dialog,_->
                        dialog.dismiss()
                    }
                    .create()
                builder.show()

                true
            }
            R.id.action_map->{
                view?.findNavController()?.navigate(R.id.action_homepage_to_mapsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setStories(story: List<ListStoryItem?>?) {

        if ((story?.size ?: 0) > 0) {
            binding.storyRv.visibility = View.VISIBLE
            binding.nodata.visibility = View.GONE
            val adapter = RecyclerViewStoryAdapter<ListStoryItem>(
                diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
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
                bindView = { item, binding ->
                    Glide.with(binding.root)
                        .load(item.photoUrl)
                        .into(binding.ivItemPhoto)
                    binding.tvItemName.text = item.name
                    binding.tvDescription.text = item.description
                },
                onClick = { item ->
                    val toDetailStory = HomeDirections.actionHomepageToDetailStory(item.id)
                    view?.findNavController()?.navigate(toDetailStory)
                }

            )

            adapter.submitList(story)
            binding.storyRv.adapter = adapter
        }

    }

}