package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map

import android.location.Location

data class MapState(
    val lastKnownLocation: Location?,
    // This is for showing warning areas later
    //val clusterItems: List<ZoneClusterItem>,
)