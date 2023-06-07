package com.uqam.mentallys.repository

import com.uqam.mentallys.network.BaseApi
import com.uqam.mentallys.network.SafeApiCall


abstract class BaseRepository(private val api: BaseApi) : SafeApiCall {

    suspend fun logout() = safeApiCall {
        api.logout()
    }
}