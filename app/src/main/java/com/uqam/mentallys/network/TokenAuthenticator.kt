package com.uqam.mentallys.network

import android.content.Context
import com.uqam.mentallys.data.UserPreferences
import com.uqam.mentallys.repository.BaseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    context: Context,
    private val tokenApi: TokenRefreshApi
) : Authenticator, BaseRepository(tokenApi) {

    private val appContext = context.applicationContext
    private val userPreferences = UserPreferences(appContext)

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = getUpdatedToken()
            response.request.newBuilder()
                .header("Authorization", "Bearer $refreshToken")
                .build()
        }
    }

    /**
     * this is should be comming from the server
     * todo
     */
    private suspend fun getUpdatedToken(): String? {
        return userPreferences.accessToken.first()
    }
}