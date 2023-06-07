package com.uqam.mentallys.data.responses

import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream

data class TestNewUser2(
    val ProfileImage: InputStream

)