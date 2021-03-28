package com.example.tabs.ui.register

import Resource
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.data.responses.Preference
import com.example.tabs.data.responses.LoginResponse
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: MainRepository
):ViewModel() {

    private val _registerResponse :MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    val registerResponse : LiveData<Resource<LoginResponse>>
        get()=_registerResponse

    private val _name :MutableLiveData<String> = MutableLiveData()
    val name : LiveData<String>
        get()=_name


    private val _email :MutableLiveData<String> = MutableLiveData()
    val email : LiveData<String>
        get()=_email


    private val _password :MutableLiveData<String> = MutableLiveData()
    val password: LiveData<String>
        get()=_password


    private val _placesPref :MutableLiveData<List<String>> = MutableLiveData()
    val placesPref : LiveData<List<String>>
        get()=_placesPref

    private val _places :MutableLiveData<List<Preference>> = MutableLiveData()
    val places : LiveData<List<Preference>>
        get()=_places





    var newlist = arrayListOf<Preference>()

    fun add(preference: Preference){
        newlist.add(preference)
        _places.value=newlist
    }

    fun remove(groupName:String){
        val index = newlist.indexOfFirst { it.group == groupName  } // -1 if not found
        if (index >= 0) {
            newlist[index].selected=!newlist[index].selected
        }
        _places.value=newlist
    }



    fun signup(
        name: String,
        email: String,
        password: String,
        places_preference: List<String>
    ) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value = repository.signup(name,email, password,places_preference)
    }

    fun setName(name: String){
        _name.value=name
    }

    fun setEmail(email: String){
        _email.value=email
    }
    fun setPassword(password: String){
        _password.value=password
    }

    fun isPreferencesEmpty():Boolean{

        return _places.value.isNullOrEmpty()
    }

    fun setPlacesPref(prefs:List<Preference>){

        var allCats:List<String> = emptyList()
        if(!prefs.isNullOrEmpty()){
            for(pref in prefs){
                if(pref.selected){
                    allCats=allCats+pref.categories

                }
            }
        }

        Log.d("Signup",allCats.toString())
        Log.d("Signup",allCats.joinToString(separator=","))

        _placesPref.value=allCats

    }

//    fun addPlace(place: String){
//        _placesPref.
//    }
    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)

    }

}
