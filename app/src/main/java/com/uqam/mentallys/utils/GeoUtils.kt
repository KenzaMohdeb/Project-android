package com.uqam.mentallys.utils

import com.google.android.gms.maps.model.LatLng

private const val TOLERANCE : Double = 0.0

@Suppress("MemberVisibilityCanBePrivate")
fun isPointInTheSquare(southWestPoint : LatLng, point : LatLng , northEastPoint : LatLng) : Boolean{
    return isPointInLatitude(southWestPoint.latitude, point.latitude, northEastPoint.latitude) && isPointInLongitude(southWestPoint.longitude, point.longitude, northEastPoint.longitude)
}

@Suppress("MemberVisibilityCanBePrivate")
fun isPointInLatitude(leftLat : Double, latitude : Double, rightLat : Double) : Boolean{
    return leftLat - TOLERANCE <= latitude && latitude <= rightLat + TOLERANCE
}

@Suppress("MemberVisibilityCanBePrivate")
fun isPointInLongitude(leftLng : Double, longitude : Double, rightLng : Double) : Boolean{
    return isPointOnTheLeft(  leftLng , longitude, rightLng) || isPointCentered(  leftLng , longitude, rightLng) || isPointOnTheRight(  leftLng , longitude, rightLng)
}

private fun isPointOnTheLeft(leftLng : Double, longitude : Double, rightLng : Double) : Boolean{
    return (longitude < rightLng + TOLERANCE && rightLng < leftLng -TOLERANCE)
}

private fun isPointCentered(leftLng : Double, longitude : Double, rightLng : Double) : Boolean{
    return (leftLng - TOLERANCE < longitude && longitude < rightLng + TOLERANCE)
}

private fun isPointOnTheRight(leftLng : Double, longitude : Double, rightLng : Double) : Boolean{
    return (rightLng + TOLERANCE < leftLng -TOLERANCE && leftLng < longitude)
}
