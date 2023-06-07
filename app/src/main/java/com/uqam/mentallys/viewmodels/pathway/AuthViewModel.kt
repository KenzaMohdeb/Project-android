package com.uqam.mentallys.viewmodels.pathway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.data.responses.CurrentUser
import com.uqam.mentallys.data.responses.LoginResponse
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: AuthRepository
) : ViewModel() {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = userRepository.login(email, password)
    }

    suspend fun saveAccessTokens(token: String, currentUser: CurrentUser) {
        userRepository.saveCurrentUser(token, currentUser)
    }

    suspend fun logout() = withContext(Dispatchers.IO) { userRepository.logout() }
}