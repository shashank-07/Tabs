package com.example.tabs.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tabs.R



import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tabs.data.network.Api
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.databinding.FragmentRegisterBinding
import com.example.tabs.ui.base.BaseFragment
import com.example.tabs.ui.enable
import com.example.tabs.ui.handleApiError
import com.example.tabs.ui.home.HomeActivity
import com.example.tabs.ui.startNewActivity

import com.example.tabs.ui.visible
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding, MainRepository>(){


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnSignup.enable(false)

        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.response.password)
                        requireActivity().startNewActivity(HomeActivity::class.java)

                    }
                }
                is Resource.Failure -> handleApiError(it) { signup() }
            }
        })

        viewModel.name.observe(viewLifecycleOwner,{
            binding.etLoginName.setText(it)
        })

        viewModel.email.observe(viewLifecycleOwner,{
            binding.etLoginEmail.setText(it)
        })



        binding.etLoginPassword.addTextChangedListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val name = binding.etLoginName.text.toString().trim()

            binding.btnSignup.enable(email.isNotEmpty() && name.isNotEmpty() && it.toString().isNotEmpty())
        }



        binding.btnSignup.setOnClickListener {


            signup()

        }
    }

    private fun signup() {
        val email = binding.etLoginEmail.text.toString().trim()
        val name = binding.etLoginName.text.toString().trim()
        val password = binding.etLoginPassword.text.toString().trim()

        viewModel.setEmail(email)
        viewModel.setName(name)
        viewModel.setPassword(password)

        nav =Navigation.findNavController(requireActivity(),R.id.fragment3)
        nav.navigate(R.id.action_registerFragment_to_preferenceFragment)
    }

    override fun getViewModel() = RegisterViewModel ::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        MainRepository(remoteDataSource.buildApi(Api::class.java), userPreferences)


}