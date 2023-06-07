package com.uqam.mentallys.network

import com.uqam.mentallys.data.responses.*
import retrofit2.http.*

interface UserApi : BaseApi {

  @GET("chat")
  suspend fun refreshAccessTokenAsync(): UserDtoItem

  @GET("user/{id}")
    suspend fun getUser(@Path("id") id:String): LoggedUser
}