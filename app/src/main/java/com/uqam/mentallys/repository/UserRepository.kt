package com.uqam.mentallys.repository

import com.uqam.mentallys.network.UserApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi
) : BaseRepository(api) {

    suspend fun refreshAccessTokenAsync()= safeApiCall {
        api.refreshAccessTokenAsync()
    }

    suspend fun getUser(id:String) = safeApiCall { api.getUser(id) }



}