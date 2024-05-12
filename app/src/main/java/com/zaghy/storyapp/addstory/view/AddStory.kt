package com.zaghy.storyapp.addstory.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.zaghy.storyapp.R
import com.zaghy.storyapp.addstory.viewmodel.AddStoryViewModel
import com.zaghy.storyapp.addstory.viewmodel.AddStoryViewModelFactory
import com.zaghy.storyapp.databinding.FragmentAddStoryBinding
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.utils.Utilities

class AddStory : Fragment() {
    private lateinit var binding:FragmentAddStoryBinding
    private var currentImageUri: Uri? = null

    //    launcher gallery
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            currentImageUri = uri
            showImage()
        } else {
            Log.d("PhotoPicker", "No media selected")
            showToast("No Media Selected")
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        val viewModel:AddStoryViewModel by viewModels<AddStoryViewModel> {
            AddStoryViewModelFactory.getInstance(requireContext())
        }
        setupAction(viewModel)
        return binding.root
    }

    private fun setupAction(viewModel: AddStoryViewModel) {
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.buttonAdd.setOnClickListener {
            Log.d(TAG, "setupAction: ")
            uploadImage(viewModel)
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.placeholder.setImageURI(it)
        }
    }

    private fun uploadImage(viewModel: AddStoryViewModel) {
        currentImageUri?.let {
            Log.d(TAG, "uploadImage: ")
            val imageFile = Utilities().uriToFile(it,requireContext())
            val description = binding.edAddDescription.text.toString()
            val latitude = 0.toFloat()
            val longitude = 0.toFloat()
            viewModel.addStory(imageFile,description,latitude,longitude).observe(viewLifecycleOwner){result->
                when(result){
                    is Result.Loading->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success->{
                        binding.progressBar.visibility = View.GONE
                        showToast(result.data.message ?: "")
                    }
                    is Result.Error->{
                        binding.progressBar.visibility = View.GONE
                        showToast(result.error)
                    }
                    null->{
//                        do nothing
                    }
                }
            }
        }
    }




    companion object {
        private const val TAG = "AddStory"
    }
}