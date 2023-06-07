package com.uqam.mentallys.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "chatResource_tbl")
@Parcelize
data class ChatResourceLoc(
    val id: UUID = UUID.randomUUID(),
    var idResource: String,
    val name: String,
    val sexe:String,
    val icon: Int,
    val resource_type: String, // TODO: Normalize
    val image: String, // TODO: See how gilt works
    val address: String,
    val postal_code: String,
    val city: String,
    val region: String,
    val country: String,
    val coordinate: String, // TODO : Find type
    val phone: String, // TODO : Verify type
    val mail: String,
    val description: String,
    val opening_hour: String,
    val disponible:String,
    val distance: String,
    val tag: List<String>,//Professionnels
    val activities: List<String>, // TODO : Normalize (Services)
    val language: String, // TODO : Normalize
    val cost: String, // TODO : Normalize
    val clients: List<String>, // TODO : Normalize
    val modalities: List<String>, // TODO : Normalize
    val CommunicationUserId: String,
    val AccessToken: String,
    val AccessTokenExpiresOn: String,
):Parcelable