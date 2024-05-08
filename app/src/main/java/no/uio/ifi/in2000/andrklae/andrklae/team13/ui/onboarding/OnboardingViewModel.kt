package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.onboarding

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.Locationdata.LocationUtil
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel

class OnboardingViewModel(context: Context) {
    // variables for completion of onboarding
    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted = _onboardingCompleted.asStateFlow()

    // to keep track of location permission
    private val _locationPermission = MutableStateFlow(false)
    val locationPermission = _locationPermission.asStateFlow()

    init {
        _onboardingCompleted.value = PreferenceManager.fetchOnboardingStatus(context)
        _locationPermission.value = LocationUtil.hasLocationPermission(context)
    }

    // function to complete onboarding
    fun completeOnboarding(context: Context){
        PreferenceManager.completeOnboarding(context)
        _onboardingCompleted.value = PreferenceManager.fetchOnboardingStatus(context)
    }

    fun locPermissionGranted() {
        _locationPermission.value = true
    }
}