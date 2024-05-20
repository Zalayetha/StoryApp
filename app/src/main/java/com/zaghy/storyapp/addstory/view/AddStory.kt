package com.zaghy.storyapp.addstory.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.zaghy.storyapp.R
import com.zaghy.storyapp.addstory.viewmodel.AddStoryViewModel
import com.zaghy.storyapp.addstory.viewmodel.AddStoryViewModelFactory
import com.zaghy.storyapp.databinding.FragmentAddStoryBinding
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.utils.getImageUri
import com.zaghy.storyapp.utils.reduceFileImage
import com.zaghy.storyapp.utils.uriToFile

class AddStory : Fragment() {
    private lateinit var binding: FragmentAddStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLatitude:Float = 0.toFloat()
    private var myLongitude:Float = 0.toFloat()

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

    //    launcher camera
    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

//    launcher permission

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(TAG,"getMyLastLocation: ${location.latitude}, ${location.longitude}")
                    myLatitude = location.latitude.toFloat()
                    myLongitude = location.longitude.toFloat()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherCamera.launch(currentImageUri)
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
        val viewModel: AddStoryViewModel by viewModels<AddStoryViewModel> {
            AddStoryViewModelFactory.getInstance(requireContext())
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupAction(viewModel)
        return binding.root
    }

    private fun setupAction(viewModel: AddStoryViewModel) {
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.cameraButton.setOnClickListener {
            startCamera()
        }
        binding.buttonAdd.setOnClickListener {
            Log.d(TAG, "setupAction: ")
            uploadImage(viewModel,latitude = myLatitude,longitude = myLongitude)
        }
       binding.myLocSwitch.setOnCheckedChangeListener { _, isChecked ->
           if(isChecked){
               getMyLastLocation()
           }else{
               myLatitude = 0.toFloat()
               myLongitude = 0.toFloat()
           }
       }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.placeholder.setImageURI(it)
        }
    }

    private fun uploadImage(viewModel: AddStoryViewModel,latitude:Float = 0.toFloat(),longitude:Float = 0.toFloat()) {
        currentImageUri?.let {
            Log.d(TAG, "uploadImage: ")
            val imageFile = uriToFile(it, requireContext()).reduceFileImage()
            val description = binding.edAddDescription.text.toString()
            viewModel.getUser().observe(viewLifecycleOwner) {

                viewModel.addStory(it.token, imageFile, description, latitude, longitude)
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                showToast(result.data.message ?: "")
                                view?.findNavController()
                                    ?.navigate(R.id.action_addStory_to_homepage)
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                showToast(result.error)
                            }

                            null -> {
//                        do nothing
                            }
                        }
                    }
            }

        }
    }


    companion object {
        private const val TAG = "AddStory"
    }
}