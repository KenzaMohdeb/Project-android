package com.uqam.mentallys.view.ui.resource.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.uqam.mentallys.R
import com.uqam.mentallys.model.resource.Category
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.utils.SharedState


class IconRenderer(
    private val context: Context, map: GoogleMap?,
    clusterManager: ResourceClusterManager?,
) : DefaultClusterRenderer<Resource>(context, map, clusterManager) {

    private val sharedState = SharedState
    private val communityIcon =
        createBitmapDescriptorFromResource(R.drawable.ic_mentallys_community)
    private val privateIcon = createBitmapDescriptorFromResource(R.drawable.ic_mentallys_private)
    private val publicIcon = createBitmapDescriptorFromResource(R.drawable.ic_mentallys_public)
    private val communityIconFilled =
        createBitmapDescriptorFromResource(R.drawable.ic_mentallys_community_filled)
    private val privateIconFilled =
        createBitmapDescriptorFromResource(R.drawable.ic_mentallys_private_filled)
    private val publicIconFilled =
        createBitmapDescriptorFromResource(R.drawable.ic_mentallys_public_filled)

    // Display the marker with a different icon depending on the type of the resource
    override fun onBeforeClusterItemRendered(item: Resource, markerOptions: MarkerOptions) {
        when (item.category) {
            Category.COMMUNITY -> markerOptions.icon(communityIcon)
            Category.PRIVATE -> markerOptions.icon(privateIcon)
            Category.PUBLIC -> markerOptions.icon(publicIcon)
        }
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    // Optimize that if you can it's called too much time it's not efficient and i just want to leave
    override fun onClusterItemRendered(clusterItem: Resource, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        sharedState.getState("MapFragmentSelectedMarker").let {
            if (it != null) {
                val item = it as Resource
                this.setMarkerAsSelected(item)
                sharedState.saveState("MapFragmentSelectedMarker", item)
            }
        }

    }

    fun setMarkerAsSelected(item: Resource) {
        when (item.category) {
            Category.COMMUNITY -> this.getMarker(item)?.setIcon(communityIconFilled)
            Category.PRIVATE -> this.getMarker(item)?.setIcon(privateIconFilled)
            Category.PUBLIC -> this.getMarker(item)?.setIcon(publicIconFilled)
        }
    }

    fun setMarkerAsUnselected(item: Resource) {
        this.getMarker(item)
        when (item.category) {
            Category.COMMUNITY -> this.getMarker(item)?.setIcon(communityIcon)
            Category.PRIVATE -> this.getMarker(item)?.setIcon(privateIcon)
            Category.PUBLIC -> this.getMarker(item)?.setIcon(publicIcon)
        }
    }


    override fun onBeforeClusterRendered(cluster: Cluster<Resource>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
    }

    // Disable the cluster system, comment if cluster system need to be restored
    /*
    override fun shouldRenderAsCluster(cluster: Cluster<Resource>): Boolean {
        return false
    }
    */

    // Change the color of the cluster
    override fun getColor(clusterSize: Int): Int {
        // There is an issue sometimes when you need to rebuild and reinstall the app on the emulator
        return ContextCompat.getColor(context, R.color.purple)
    }

    // https://stackoverflow.com/a/65421222
    private fun createBitmapDescriptorFromResource(resourceId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, resourceId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        vectorDrawable.draw(Canvas(bitmap))
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}