package com.zaghy.storyapp.map.view

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.zaghy.storyapp.R
import com.zaghy.storyapp.databinding.FragmentMapsBinding
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.map.viewmodel.MapViewModel
import com.zaghy.storyapp.map.viewmodel.MapViewModelFactory
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.utils.bitmapToBitmapDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    val viewModel: MapViewModel by viewModels<MapViewModel> {
        MapViewModelFactory.getInstance(requireContext())
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    companion object {
        private const val TAG = "MapsFragment"
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        //        enable some of control
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        val jakarta = LatLng(-6.2088, 106.8456)
        googleMap.addMarker(MarkerOptions().position(jakarta).title("Marker in Jakarta"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta))
        googleMap.addMarker(MarkerOptions().position(jakarta))

        getMyLocation()
        setMapStyle()
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            viewModel.getStoriesByLocation(token = user.token, location = 1)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
//                        do nothing
                            Log.d(TAG, "Loading")
                        }

                        is Result.Success -> {
                            Log.d(TAG, "Success")
                            addManyMarker(result.data.listStory)
                        }

                        is Result.Error -> {
                            Log.d(TAG, result.error)
                        }

                        else -> {
//                        do nothing
                        }
                    }
                }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun addManyMarker(data: List<ListStoryItem?>?) {
        lifecycleScope.launch {
            data?.forEach { dt ->
                val latLng = LatLng(dt?.lat ?: 0.0, dt?.lon ?: 0.0)
                val bitmap = withContext(Dispatchers.IO) { // Switch to background thread
                    val futureTarget: FutureTarget<Bitmap> =
                        Glide.with(requireContext()).asBitmap().load(dt?.photoUrl).submit(80, 80)
                    try {
                        futureTarget.get() // Perform blocking call on background thread
                    } finally {
                        Glide.with(requireContext())
                            .clear(futureTarget) // Clean up to avoid memory leaks
                    }
                }
                mMap.addMarker(
                    MarkerOptions()
                        .icon(bitmapToBitmapDescriptor(bitmap))
                        .position(latLng)
                        .title(dt?.name)
                        .snippet(dt?.description)
                )
            }
        }
    }


    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}