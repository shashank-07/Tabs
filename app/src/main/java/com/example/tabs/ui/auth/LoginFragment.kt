package com.example.tabs.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.tabs.databinding.FragmentLoginBinding
import com.example.tabs.data.network.AuthApi
import com.example.tabs.data.repository.AuthRepository
import com.example.tabs.ui.base.BaseFragment
import com.example.tabs.ui.enable
import com.example.tabs.ui.home.HomeActivity
import com.example.tabs.ui.startNewActivity
import com.example.tabs.ui.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel,FragmentLoginBinding,AuthRepository>(){

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.btnLogin.enable(false)
        binding.progressbar.visible(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(false)

            when (it) {
                is Resource.Success -> {

                        viewModel.saveAuthToken(it.value.response.password)
                        requireActivity().startNewActivity(HomeActivity::class.java)

                    Toast.makeText(requireContext(), it.value.response.toString(), Toast.LENGTH_LONG).show()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.etLoginPassword.addTextChangedListener{
            val email=binding.etLoginUsername.text.toString().trim()
            binding.btnLogin.enable(email.isNotEmpty()&& it.toString().isNotEmpty())
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginUsername.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()
            binding.progressbar.visible(true)
            viewModel.login(email,password)
        }

    }

    override fun getViewModel()=AuthViewModel::class.java

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    )= FragmentLoginBinding.inflate(inflater,container,false)


    override fun getFragmentRepository() = AuthRepository(remoteDataSource.buildApi(AuthApi::class.java),userPreferences)


}