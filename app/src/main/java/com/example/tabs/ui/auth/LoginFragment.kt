package com.example.tabs.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.tabs.databinding.FragmentLoginBinding
import com.example.tabs.data.network.Api
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.ui.base.BaseFragment
import com.example.tabs.ui.enable
import com.example.tabs.ui.handleApiError
import com.example.tabs.ui.home.HomeActivity
import com.example.tabs.ui.register.RegisterActivity
import com.example.tabs.ui.startNewActivity
import com.example.tabs.ui.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel,FragmentLoginBinding,MainRepository>(){


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            var navController: NavController?=null
            binding.progressbar.visible(false)
            binding.btnLogin.enable(false)

            viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
                binding.progressbar.visible(it is Resource.Loading)
                when (it) {
                    is Resource.Success -> {
                        lifecycleScope.launch {
                            viewModel.saveAuthToken(it.value.response.password!!)
                         requireActivity().startNewActivity(HomeActivity::class.java)

                        }
                    }
                    is Resource.Failure -> handleApiError(it) { login() }
                }
            })

            binding.etLoginPassword.addTextChangedListener {
                val email = binding.etLoginUsername.text.toString().trim()
                binding.btnLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
            }


            binding.btnLogin.setOnClickListener {
                login()
            }
            binding.btnSignup.setOnClickListener{
                requireActivity().startNewActivity(RegisterActivity::class.java)
            }
        }

        private fun login() {
            val email = binding.etLoginUsername.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        override fun getViewModel() = AuthViewModel::class.java

        override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
        ) = FragmentLoginBinding.inflate(inflater, container, false)

        override fun getFragmentRepository() =
            MainRepository(remoteDataSource.buildApi(Api::class.java), userPreferences)


    }