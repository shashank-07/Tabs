package com.example.tabs.ui.home

import Resource
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.data.responses.ClusterResponse
import com.example.tabs.data.responses.Itenary
import com.example.tabs.data.responses.ItenaryResponse
import com.example.tabs.data.responses.Location
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MainRepository
):ViewModel() {

    private val _clusterResponse :MutableLiveData<Resource<ClusterResponse>> = MutableLiveData()
    val clusterResponse : LiveData<Resource<ClusterResponse>>
        get()=_clusterResponse

    private val _itenaryResponse :MutableLiveData<Resource<ItenaryResponse>> = MutableLiveData()
    val itenaryResponse : LiveData<Resource<ItenaryResponse>>
        get()=_itenaryResponse

    private val _userLoc :MutableLiveData<Location> = MutableLiveData()
    val userLoc : LiveData<Location>
        get()=_userLoc


    private val _userCluster :MutableLiveData<Int> = MutableLiveData()
    val userCluster : LiveData<Int>
        get()=_userCluster

    private val _budget :MutableLiveData<Int> = MutableLiveData()
    val budget : LiveData<Int>
        get()=_budget

    private val _time :MutableLiveData<Int> = MutableLiveData()
    val time : LiveData<Int>
        get()=_time

    private val _refresh :MutableLiveData<Int> = MutableLiveData()
    val refresh : LiveData<Int>
        get()=_refresh

    private val _itenary :MutableLiveData<List<Itenary>> = MutableLiveData()
    val itenary : LiveData<List<Itenary>>
        get()=_itenary

    fun setBudget(budget:Int){
        _budget.value=budget
    }

    fun setTime(time:Int){
        _time.value=time
    }

    fun setRefresh(refresh:Int){
        _refresh.value=refresh
    }


    fun setCluster(cluster: Int){
        _userCluster.value=cluster
    }

    fun setItenary(itenary: List<Itenary>){
        _itenary.value=itenary
    }

    fun getCluster(
        latitude: Double,
        longitude: Double
    ) = viewModelScope.launch {
        _clusterResponse.value = Resource.Loading
        _clusterResponse.value = repository.getCluster(latitude, longitude)
    }

    fun getItenary(
        budget: Int,
        time: Int,
        refresh:Int,
        cluster:Int
    ) = viewModelScope.launch {
        _itenaryResponse.value = Resource.Loading
        //val response = repository.getItenary(budget,time,refresh,cluster);
        _itenaryResponse.value = repository.getItenary(budget,time,refresh,cluster)
        Log.d("MOFOS",_itenaryResponse.value.toString())

    }



    fun setLoc(
        lat:Double,
        long:Double
    ){

        val loc=Location(lat,long)
        Log.d("Yeahh","${loc.latitude}, ${loc.longitude} yeaah")
        _userLoc.value=loc
    }

    suspend fun saveCluster(cluster: Int) {
        repository.saveCluster(cluster)
    }

}

