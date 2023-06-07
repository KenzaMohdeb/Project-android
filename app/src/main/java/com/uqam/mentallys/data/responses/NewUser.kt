package com.uqam.mentallys.data.responses

import android.net.Uri
import okhttp3.MultipartBody
import java.io.File
import java.io.InputStream

data class NewUser(
    val Firstname: String,
    val Lastname: String,
    val Email: String,
    val RoleName: String,
    val ProfileImage: MultipartBody.Part

)