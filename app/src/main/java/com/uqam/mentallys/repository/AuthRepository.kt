package com.uqam.mentallys.repository

import com.uqam.mentallys.data.UserPreferences
import com.uqam.mentallys.data.responses.CurrentUser
import com.uqam.mentallys.data.responses.ForgetPassword
import com.uqam.mentallys.data.responses.LoginUser
import com.uqam.mentallys.network.AuthApi
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository(authApi) {

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        authApi.login(LoginUser(email, password))
    }
    suspend fun passwordForgotten(
        email: String
    ) = safeApiCall {
        authApi.passwordForgotten(ForgetPassword(email))
    }
    suspend fun createAccount(
        firstName:String,
        lastName:String,
        email: String,
        password : String,
        passwordConfirm:String,
        photo: MultipartBody.Part?
    )= safeApiCall {
        authApi.register(email,password, passwordConfirm, firstName, lastName, photo)
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        preferences.saveAccessTokens(accessToken, refreshToken)
    }
    suspend fun saveCurrentUser(token:String, currentUser: CurrentUser) {
        preferences.saveCurrentUser(token, currentUser)
    }
}