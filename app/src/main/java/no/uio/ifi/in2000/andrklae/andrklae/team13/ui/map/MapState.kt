package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.map

import android.location.Location
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.CustomLocation
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.warnings.Feature

data class MapState(
    val lastKnownLocation: CustomLocation,
    // This is for showing warning areas later
    val warnings: Feature
)