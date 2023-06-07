package com.uqam.mentallys.network

import com.uqam.mentallys.data.responses.TokenResponse
import retrofit2.http.POST

interface TokenRefreshApi : BaseApi {

    /* we should provide an refresh token from the server */
    @POST("authentication/refresh-token")
    suspend fun refreshAccessToken(): TokenResponse
}