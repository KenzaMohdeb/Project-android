package com.uqam.mentallys.view.ui.message

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedUserInfo(
    var id:String?,
    var fullUserName:String?,
    val userImage:String?,
    val email:String?,
    val CommunicationUserId: String?,
    val AccessToken: String?,
    val AccessTokenExpiresOn: String?,
):Parcelable
