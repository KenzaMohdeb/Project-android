package com.uqam.mentallys.view.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentPasswordForgottenBinding
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.utils.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordForgottenFragment : Fragment(R.layout.fragment_password_forgotten) {

    private lateinit var binding: FragmentPasswordForgottenBinding
    private val viewModel by viewModels<AuthViewModel>()
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPasswordForgottenBinding.bind(view)
        navController = Navigation.findNavController(view)
        //if the account is already created
        binding.textViewRegisterNow.setOnClickListener {
            navController!!.navigate(R.id.registerFragment)
        }
        binding.loginForgettenPasword.setOnClickListener {
            if(binding.fgUserEmail.text.isNotEmpty())
                passwordForgotten(binding.fgUserEmail.text.toString())
        }
        observerPasswordForgotten()
    }
    private fun passwordForgotten(email:String) {
        viewModel.passwordForgotten(email)
    }
    private fun observerPasswordForgotten() {
        viewModel.ForgottenPWResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        navController!!.navigate(R.id.loginFragment)
                    }
                }
                is Resource.Failure -> handleApiError(it) { Toast.makeText(context,getString(R.string.errorSendingPasswordForgotten), Toast.LENGTH_LONG).show() }
                else -> handleApiError(it as Resource.Failure) { Toast.makeText(context,getString(R.string.errorSendingPasswordForgotten), Toast.LENGTH_LONG).show() }
            }
        }
    }
}