package com.example.tabs.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color

import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabs.R
import com.example.tabs.data.network.Api
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.data.responses.Itenary
import com.example.tabs.databinding.FragmentHomeBinding
import com.example.tabs.ui.*
import com.example.tabs.ui.adapters.ItenaryRecyclerAdapter
import com.example.tabs.ui.adapters.PreferenceRecyclerAdapter
import com.example.tabs.ui.auth.AuthActivity
import com.example.tabs.ui.base.BaseFragment
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment  : BaseFragment<HomeViewModel, FragmentHomeBinding, MainRepository>(){
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010

     var userLocation:com.example.tabs.data.responses.Location=com.example.tabs.data.responses.Location(0.0,0.0)
     var user_cluster:Int = -1

    var budget:Int = -1
    var time:Int = -1
    var refresh:Int = 0
    var api_being_called:Boolean=false
    var places_pref:String=""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        closeCustom()
        intialize()
        checkLocal()
        checkLocalPref()

        viewModel.clusterResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {

                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveCluster(it.value.response.cluster)
                        Log.d("Callll","1")
                        binding.btBuild.performClick()
                    }
                }
                is Resource.Failure ->{

                    handleApiError(it) { getCluster(userLocation) }
                }
            }
        })



        viewModel.itenaryResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {

                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.setItenary(it.value.itenary)
                        api_being_called=false
                        Log.d("Hi",it.value.toString())
                    }
                }
                is Resource.Failure ->{

                    handleApiError(it) {
                        api_being_called=false

                        buildItenary()
                    }
                }
            }
        })



        viewModel.itenary.observe(viewLifecycleOwner,{
          val adapter=ItenaryRecyclerAdapter(viewModel, it, requireContext())
              adapter.setHasStableIds(true);
            binding.placesList.adapter=adapter

        })

        viewModel.userLoc.observe(viewLifecycleOwner, {
            userLocation=it;
           getCluster(userLocation)

        })
        viewModel.userCluster.observe(viewLifecycleOwner,{
            user_cluster=it
        })
        viewModel.budget.observe(viewLifecycleOwner,{
            budget=it
        })
        viewModel.time.observe(viewLifecycleOwner,{
            time=it
        })
        viewModel.refresh.observe(viewLifecycleOwner,{
            refresh=it
            binding.btBuild.performClick()
        })

        viewModel.userPref.observe(viewLifecycleOwner,{
            places_pref=it
        })


        binding.refresh.setOnClickListener {
            viewModel.setRefresh(refresh+1);
        }
        binding.btBuild.setOnClickListener{
            if(user_cluster==-1){
                RequestPermission()
                getLastLocation()

            }else{
                buildItenary()
                closeCustom()
            }

        }



        binding.customize.setOnClickListener {



                if (binding.hiddenView.visibility == View.VISIBLE) {


                    TransitionManager.beginDelayedTransition(binding.mainCard,
                        AutoTransition())
                    binding.hiddenView.visibility = View.GONE

                } else {
                    TransitionManager.beginDelayedTransition(binding.mainCard,
                        AutoTransition())
                    binding.hiddenView.visibility = View.VISIBLE
                }



        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())


    }

    fun closeCustom(){
        binding.hiddenView.visibility=View.GONE

    }
    private fun intialize(){
        binding.progressbar.visible(false)
        binding.placesList.layoutManager= LinearLayoutManager(requireContext())


    }


    private fun getCluster(userLocation:com.example.tabs.data.responses.Location) {
        if(!userLocation.equals(null)){
            if(userLocation.latitude!=0.0){
                viewModel.getCluster(userLocation.latitude, userLocation.longitude)

            }else{
                view?.snackbar("Your location has not been found \nUse default location to by pass this")
            }
        }


    }


    fun checkLocal(){
        userPreferences.cluster.asLiveData().observe(requireActivity(), androidx.lifecycle.Observer {
            Log.d("local","got cluster"+it.toString())
            if(it!=null){
                viewModel.setCluster(it)
            }

        })

    }
    fun checkLocalPref(){
        userPreferences.authToken.asLiveData().observe(requireActivity(), androidx.lifecycle.Observer {
            Log.d("local","got cluster"+it.toString())
            if(it!=null){
                viewModel.setUserPref(it)
            }

        })

    }


    private fun buildItenary(){

        if(binding.etBudget.text.toString()!=""){
            budget=binding.etBudget.text.toString().trim().toInt()

        }
        if(binding.etTime.text.toString()!=""){
            time=binding.etTime.text.toString().trim().toInt()

        }

        if(budget!=-1&&time!=-1&&user_cluster!=-1){
            if(!api_being_called){
                api_being_called=true

                viewModel.getItenary(budget,time,refresh,user_cluster,places_pref)

            }

        }else{
            view?.snackbar("Did not get budget/time/location")

        }

    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        viewModel.setLoc(location.latitude,location.longitude)
                        Log.d("Debug:" , "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude)
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            viewModel.setLoc(lastLocation.latitude,lastLocation.longitude)
            Log.d("Debug:" , "You Current Location is Result : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude)

        }
    }

    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )

    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }



    override fun getViewModel() = HomeViewModel ::class.java

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    )= FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
            MainRepository(remoteDataSource.buildApi(Api::class.java), userPreferences)


}

