package com.uqam.mentallys.model

import com.google.android.gms.maps.model.LatLng

enum class Country(val coordinate : LatLng, val zoom: Float) {
    FRA(LatLng(46.9383612,2.2244547),5F),
    USA(LatLng(35.7234391,-92.1787285),3F),
    CAN(LatLng(58.3610581,-99.4189993),2.25F)
}