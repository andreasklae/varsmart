package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.onboarding

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.andrklae.andrklae.team13.Data.PreferenceManager
import no.uio.ifi.in2000.andrklae.andrklae.team13.ui.Settings.SettingsViewModel

class OnboardingViewModel(context: Context) {
    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted = _onboardingCompleted.asStateFlow()
    init {
        _onboardingCompleted.value = PreferenceManager.fetchOnboardingStatus(context)
    }

    fun startOnboarding(context: Context){
        PreferenceManager.startOnboarding(context)
        _onboardingCompleted.value = PreferenceManager.fetchOnboardingStatus(context)
    }

    // function to complete onboarding
    fun completeOnboarding(context: Context){
        PreferenceManager.completeOnboarding(context)
        _onboardingCompleted.value = PreferenceManager.fetchOnboardingStatus(context)
    }
}