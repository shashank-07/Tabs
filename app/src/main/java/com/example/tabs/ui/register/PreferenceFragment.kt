package com.example.tabs.ui.register


import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback


import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabs.R
import com.example.tabs.data.network.Api
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.data.responses.Preference
import com.example.tabs.databinding.FragmentPreferenceBinding
import com.example.tabs.ui.*
import com.example.tabs.ui.adapters.PreferenceRecyclerAdapter
import com.example.tabs.ui.base.BaseFragment
import com.example.tabs.ui.home.HomeActivity

import kotlinx.coroutines.launch

class PreferenceFragment : BaseFragment<RegisterViewModel, FragmentPreferenceBinding, MainRepository>(){

    private lateinit var email:String
    private lateinit var name:String
    private lateinit var password:String
    private  var placesPref:List<String> = emptyList()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressbar.visible(false)
        binding.btnSignup.enable(true)


        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {

                is Resource.Success -> {
                    lifecycleScope.launch {

                        viewModel.saveAuthToken(it.value.response.places_preference.joinToString(separator = ","))
                        requireActivity().startNewActivity(HomeActivity::class.java)

                    }
                }
                is Resource.Failure -> handleApiError(it) { signup() }
            }
        })

        viewModel.email.observe(viewLifecycleOwner,{
            email=it
        })

        viewModel.name.observe(viewLifecycleOwner,{
            name=it
        })
        viewModel.password.observe(viewLifecycleOwner,{
            password=it
        })

        viewModel.placesPref.observe(viewLifecycleOwner,{
            placesPref=it
        })

        intialize()

        viewModel.places.observe(viewLifecycleOwner, Observer{

            binding.placesList.adapter=PreferenceRecyclerAdapter(viewModel, it, requireContext())

            viewModel.setPlacesPref(it)


        })

        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setPassword("");
                nav = Navigation.findNavController(requireActivity(), R.id.fragment3)
                nav.navigate(R.id.action_preferenceFragment_to_registerFragment)
            }
        })

        binding.btnSignup.setOnClickListener {


            signup()

        }

    }



    private fun intialize(){
        binding.placesList.layoutManager=LinearLayoutManager(requireContext())
        addCategories()

    }

    private fun addCategories(){
        if(viewModel.isPreferencesEmpty()){

            //Manually Add all groups here
            viewModel.add(Preference("Shashank",listOf("a", "b", "c"),false))
            viewModel.add(Preference("Abhishek",listOf("d", "e", "f"),false))
            viewModel.add(Preference("Ayush",listOf("g", "h", "i"),false))
            viewModel.add(Preference("Prashant",listOf("j", "k", "l"),false))
            viewModel.add(Preference("Prabhu",listOf("m", "n", "o"),false))
            viewModel.add(Preference("Reuben",listOf("p", "q", "r"),false))
            viewModel.add(Preference("Ventura",listOf("s", "t", "u"),false))
        }



    }

    private fun signup() {

        if(email.isNotEmpty()&&name.isNotEmpty()&&password.isNotEmpty()){
            viewModel.signup(name,email, password,placesPref)

        }else{
            view?.snackbar("Please fill all fields")
        }




    }

    override fun getViewModel() = RegisterViewModel ::class.java

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    )=FragmentPreferenceBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
            MainRepository(remoteDataSource.buildApi(Api::class.java), userPreferences)


}

