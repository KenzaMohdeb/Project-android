package com.uqam.mentallys.viewmodels.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.data.UserPreferences
import com.uqam.mentallys.data.responses.LoggedUser
import com.uqam.mentallys.data.responses.LoginUserInfo
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel()  {

    private val _user: MutableLiveData<Resource<LoggedUser>> = MutableLiveData()
    val user: LiveData<Resource<LoggedUser>>
        get() = _user

    private val _userInfo: MutableLiveData<LoginUserInfo> = MutableLiveData()
    val userInfo: LiveData<LoginUserInfo>
        get() = _userInfo

    fun getUser(id:String) = viewModelScope.launch{
        _user.value = Resource.Loading
        _user.value = repository.getUser(id)
    }
    suspend fun readToken():String?{
        return userPreferences.accessToken.first()
    }
    suspend fun readUserId():String?{
        return userPreferences.userID.first()
    }

    // Won't compile if not there, linter throw issue relative to nullable type
    @Suppress("c", "UNNECESSARY_NOT_NULL_ASSERTION")
    fun getUserInfo()= viewModelScope.launch{
        userPreferences.getUserInfo().let {
            _userInfo.value = it.first()!!
        }
    }

    fun logout()= viewModelScope.launch{
        userPreferences.clear()
        //_userInfo=null
    }
   // suspend fun logout() = withContext(Dispatchers.IO) { repository.logout() }

    fun performLogout() = viewModelScope.launch {
      //  logout()
        userPreferences.clear()
        //startNewActivity(AuthActivity::class.java)
    }
}