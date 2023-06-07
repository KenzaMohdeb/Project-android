package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.uqam.mentallys.model.Days
import java.io.Serializable
import java.util.*

/*
 * Definition of resource type
 */
@Keep
@Entity(tableName = "resources")
data class Resource(
    @ColumnInfo(name = "resource_id")
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "postal_code") val postalCode: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "mail") val mail: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "opening_hour") val openingHour: List<Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>>?,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "activities") val activities: List<Activity>,
    @ColumnInfo(name = "language") val languages: List<Language>,
    @ColumnInfo(name = "cost") val cost: Cost,
    @ColumnInfo(name = "clients") val clients: List<Client>,
    @ColumnInfo(name = "modalities") val modalities: List<Modality>,
) : ClusterItem, Serializable {

    var index :List<String> = listOf()

    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return description
    }
}