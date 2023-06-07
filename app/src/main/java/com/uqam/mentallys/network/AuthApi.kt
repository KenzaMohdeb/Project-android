package com.uqam.mentallys.network

import com.uqam.mentallys.data.responses.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthApi : BaseApi {

    @POST("authentication/login")
    suspend fun login(@Body loginUser: LoginUser
    ): LoginResponse

    @POST("authentication/passwordforgotten")
    suspend fun passwordForgotten(@Body forgetPassword: ForgetPassword): Int

    @Multipart
    @POST("authentication/register")
    suspend fun register(@Part("Email") email:String, @Part("Password") password:String, @Part("PasswordConfirmation") PasswordConfirmation:String, @Part("Firstname") firstname:String, @Part("Lastname") lastName:String, @Part ProfileImage: MultipartBody.Part?)

}
