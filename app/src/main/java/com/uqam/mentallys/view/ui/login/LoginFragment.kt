package com.uqam.mentallys.view.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentLoginBinding
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.utils.enable
import com.uqam.mentallys.utils.handleApiError
import com.uqam.mentallys.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<AuthViewModel>()
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        binding.progressbar.visible(false)

        binding.login.enable(false)

        navController = Navigation.findNavController(view)

        observerLoginInfo()
        observerSaveLoginInfo()

        binding.login.setOnClickListener {
            login()
        }

        binding.textViewRegisterNow.setOnClickListener {
            navController!!.navigate(R.id.registerFragment)
        }

        binding.textViewForgotPassword.setOnClickListener {
            navController!!.navigate(R.id.passwordForgottenFragment)
        }

        binding.editTextPassword.addTextChangedListener {
            val email = binding.editTextUsername.text.toString().trim()
            binding.login.enable(email.isNotEmpty())
        }

    }

    private fun observerSaveLoginInfo() {
        viewModel.SaveUserLogin.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                navController!!.navigate(R.id.mainChatFragment)
            }
        }
    }

    private fun observerLoginInfo() {
        binding.progressbar.visible(false)
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveUserInfo(it.value.token, it.value.currentUser)
                    }
                }
                else -> { Toast.makeText(context, "Désolé. Code d'utilisateur ou mot de passe incorrect.", Toast.LENGTH_LONG).show() }
            }
        }
    }

    private fun login() {
        binding.progressbar.visible(true)
        val email = binding.editTextUsername.text.toString() //"administrator@gmail.com"
        val password = binding.editTextPassword.text.toString() //"Password123#"
        viewModel.login(email, password)
    }
}
