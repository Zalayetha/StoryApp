package com.zaghy.storyapp.map.view

import android.content.pm.PackageManager
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
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zaghy.storyapp.R
import com.zaghy.storyapp.databinding.FragmentMapsBinding
import com.zaghy.storyapp.home.model.ListStoryItem
import com.zaghy.storyapp.map.viewmodel.MapViewModel
import com.zaghy.storyapp.map.viewmodel.MapViewModelFactory
import com.zaghy.storyapp.network.Result

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var  binding : FragmentMapsBinding
    val viewModel : MapViewModel by viewModels<MapViewModel>{
        MapViewModelFactory.getInstance(requireContext())
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted: Boolean ->
        if(isGranted){
            getMyLocation()
        }
    }

    companion object{
        private const val TAG = "MapsFragment"
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        //        enable some of control
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true


        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        getMyLocation()
        viewModel.getUser().observe(viewLifecycleOwner){user->
            viewModel.getStoriesByLocation(token = user.token, location = 1).observe(viewLifecycleOwner){result->
                when(result){
                    is Result.Loading->{
//                        do nothing
                        Log.d(TAG,"Loading")
                    }
                    is Result.Success->{
                        Log.d(TAG,"Success")
                        addManyMarker(result.data.listStory)
                    }
                    is Result.Error->{
                        Log.d(TAG,result.error)
                    }
                    else->{
//                        do nothing
                    }
                }
            }
        }
    }

    private fun addManyMarker(data: List<ListStoryItem?>?) {

        data?.forEach { dt->
            val latLng = LatLng(dt?.lat ?: 0.0,dt?.lon ?: 0.0)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(dt?.name)
                    .snippet(dt?.description)
            )
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
        inflater.inflate(R.menu.map_options,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
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
            else->{
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