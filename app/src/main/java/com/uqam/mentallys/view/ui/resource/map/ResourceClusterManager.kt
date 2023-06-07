package com.uqam.mentallys.view.ui.resource.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.uqam.mentallys.model.resource.Resource


class ResourceClusterManager(context: Context, googleMap: GoogleMap) :
    ClusterManager<Resource>(context, googleMap) {

    init {
        this.setAnimation(false)
        this.renderer = IconRenderer(context, googleMap, this)
    }

}