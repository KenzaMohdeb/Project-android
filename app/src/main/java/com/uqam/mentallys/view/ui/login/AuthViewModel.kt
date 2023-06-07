package com.uqam.mentallys.view.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.data.UserPreferences
import com.uqam.mentallys.data.responses.CurrentUser
import com.uqam.mentallys.data.responses.LoginResponse
import com.uqam.mentallys.data.responses.LoginUserInfo
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: AuthRepository,
    private val userPreferences: UserPreferences
): ViewModel()  {

    private val _userInfo: MutableLiveData<LoginUserInfo> = MutableLiveData()
    val userInfo: LiveData<LoginUserInfo>
        get() = _userInfo

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _CreateUserResponse: MutableLiveData<Resource<Any>> = MutableLiveData()
    val CreateUserResponse: LiveData<Resource<Any>>
        get() = _CreateUserResponse

    private val _SaveUserLogin: MutableLiveData<String> = MutableLiveData()
    val SaveUserLogin: LiveData<String>
        get() = _SaveUserLogin

    private val _ForgottenPWResponse: MutableLiveData<Resource<Int>> = MutableLiveData()
    val ForgottenPWResponse: LiveData<Resource<Int>>
        get() = _ForgottenPWResponse

    fun getUserInfo() = viewModelScope.launch{
        userPreferences.getUserInfo().let {
            _userInfo.value = it.first()
        }
    }
    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = userRepository.login(email, password)
    }

    fun passwordForgotten(
        email: String
    ) = viewModelScope.launch {
        _ForgottenPWResponse.value = userRepository.passwordForgotten(email)
    }

    fun createAccount(
        firstName:String,
        lastName:String,
        email: String,
        password: String,
        passwordConfirm:String,
        profileImage: File?
    ) = viewModelScope.launch {
        //_loginResponse.value = Resource.Loading
        val photoContent: RequestBody? =
            profileImage?.asRequestBody("*/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part? =
            photoContent?.let {
                MultipartBody.Part.createFormData(
                    "ProfileImage",
                    profileImage.name,
                    it
                )
            }
        val result = userRepository.createAccount(firstName, lastName,email,password, passwordConfirm, photo)
        _CreateUserResponse.value = result
    }
    fun saveUserInfo(token:String, currentUser:CurrentUser)= viewModelScope.launch{
        userPreferences.saveCurrentUser(token,currentUser)
        _SaveUserLogin.value ="saved"
    }

}