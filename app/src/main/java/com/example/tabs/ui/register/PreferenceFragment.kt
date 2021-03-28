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
import androidx.recyclerview.widget.GridLayoutManager
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
                viewModel.setPassword("")
                nav = Navigation.findNavController(requireActivity(), R.id.fragment3)
                nav.navigate(R.id.action_preferenceFragment_to_registerFragment)
            }
        })

        binding.btnSignup.setOnClickListener {
            signup()
        }
    }



    private fun intialize(){
        binding.placesList.layoutManager=GridLayoutManager(requireContext(),2)
        addCategories()
    }

    private fun addCategories(){
        if(viewModel.isPreferencesEmpty()){
            //Manually Add all groups here
            viewModel.add(Preference("Religious Sites",listOf("Historic Sites Religious Sites","Religious Sites","Religious Sites Churches & Cathedrals","Missions Religious Sites", "Architectural Buildings Religious Sites", "Tourist points & Landmarks Religious Sites","Churches & Cathedrals"),false))
            viewModel.add(Preference("Historic Sites",listOf("Speciality Museums", "Historic Sites", "Tourist points & Landmarks Architectural Buildings",),false))
            viewModel.add(Preference("Nature and Wildlife",listOf("Aquariums", "Nature & Wildlife Areas", "Parks Jogging Paths & Tracks","Bodies of Water","Zoos","Gardens","National Parks"),false))
            viewModel.add(Preference("\nBeaches",listOf("Beaches"),false))
            viewModel.add(Preference("Shopping Centers",listOf("Tourist points & Landmarks Flea & Street Markets", "Flea & Street Markets", "Antique Shops"),false))
            viewModel.add(Preference("Localities",listOf("Neighborhoods"),false))
            viewModel.add(Preference("Movie Theaters",listOf("Movie Theaters Shopping Malls"),false))
            viewModel.add(Preference("Tourist Spots",listOf("Tourist points & Landmarks Parks","Tourist Spots", "Tourist points & Landmarks Lighthouses", "Libraries","Tourist points & Landmarks","Tourist points & Landmarks Monuments & Statues"),false))
            viewModel.add(Preference("Sports & Fitness",listOf("Golf Courses","Arenas & Stadiums","Sports Complexes", "Parks Jogging Paths & Tracks", "Sports Camps & Clinics Sports Complexes"),false))
            viewModel.add(Preference("Art and Culture",listOf("Art Galleries","Art Museums", "Libraries", "Paint & Pottery Studios","Paint & Pottery Studios Lessons & Workshops","Theaters Theatre & Performances"),false))
            viewModel.add(Preference("Architectural Buildings",listOf("Architectural Buildings", "Bridges"),false))
            viewModel.add(Preference("Random Fun & Adventure",listOf("tours","experiences","adventure","Science Museums", "Speciality & Gift Shops"),false))
            viewModel.add(Preference("Games & Entertainment",listOf("Miniature Golf","Escape Games", "Playgrounds Game & Entertainment Centers", "Bowling Alleys Game & Entertainment Centers"),false))
            viewModel.add(Preference("\nHealth & Spas",listOf("Yoga & Pilates", "Health Clubs Spas", "Health Clubs Hammams & Turkish Baths","Health Clubs"),false))
            viewModel.add(Preference("Resorts",listOf("resorts", "Health Clubs Spas", "Health Clubs Hammams & Turkish Baths","Health Clubs"),false))
            viewModel.add(Preference("Luxury",listOf("luxury"),false))
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

