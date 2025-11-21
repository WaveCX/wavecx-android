package com.wavecx.sdkworkspace

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wavecx.androidsdk.Content
import com.wavecx.androidsdk.WaveCx
import com.wavecx.androidsdk.WaveCxError
import com.wavecx.androidsdk.WaveCxListener
import java.util.*

data class AnalyticsEvent(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Date = Date(),
    val type: String,
    val details: String
)

/**
 * Simplified ViewModel that manages session state and coordinates SDK listener.
 * Screens call the SDK directly for clarity.
 */
class WaveCxViewModel : ViewModel() {
    // Session state
    var isSessionActive by mutableStateOf(false)
        private set
    var currentUserId by mutableStateOf<String?>(null)
        private set

    // Content loading state - true while initial content is being fetched
    var isLoadingContent by mutableStateOf(false)
        private set

    // Content availability state - updated when SDK calls onContentChanged()
    val triggerPointsWithContent = mutableStateOf<Set<String>>(emptySet())

    // Optional analytics for debugging
    val analyticsEvents = mutableStateListOf<AnalyticsEvent>()

    fun setupListener(waveCx: WaveCx) {
        waveCx.setListener(object : WaveCxListener {
            override fun onContentReceived(content: List<Content>) {
                isLoadingContent = false  // Content loading complete
                addAnalyticsEvent("Content Received", "Loaded ${content.size} content item(s)")
                // Update available trigger points
                triggerPointsWithContent.value = waveCx.getTriggerPointsWithContent("popup")
            }

            override fun onContentPresented(content: Content) {
                addAnalyticsEvent("Content Presented", "Trigger: ${content.triggerPoint}")
            }

            override fun onContentDismissed(content: Content) {
                addAnalyticsEvent("Content Dismissed", "Trigger: ${content.triggerPoint}")
            }

            override fun onContentChanged() {
                addAnalyticsEvent("Content Changed", "Visibility updated")
                // Update available trigger points when content is marked as seen
                triggerPointsWithContent.value = waveCx.getTriggerPointsWithContent("popup")
            }

            override fun onError(error: WaveCxError) {
                isLoadingContent = false  // Stop loading on error
                addAnalyticsEvent("Error", "${error.type}: ${error.message}")
            }
        })
    }

    fun startSession(waveCx: WaveCx, userId: String, userAttributes: Map<String, Any>? = null) {
        isLoadingContent = true  // Start loading content
        waveCx.startUserSession(userId, userAttributes = userAttributes)
        isSessionActive = true
        currentUserId = userId
        addAnalyticsEvent("Session Started", "User: $userId")
    }

    fun endSession(waveCx: WaveCx) {
        waveCx.endUserSession()
        isSessionActive = false
        isLoadingContent = false
        currentUserId = null
        triggerPointsWithContent.value = emptySet()
        addAnalyticsEvent("Session Ended", "User logged out")
    }

    fun clearAnalytics() {
        analyticsEvents.clear()
    }

    private fun addAnalyticsEvent(type: String, details: String) {
        analyticsEvents.add(0, AnalyticsEvent(type = type, details = details))
        if (analyticsEvents.size > 50) {
            analyticsEvents.removeAt(analyticsEvents.size - 1)
        }
    }
}
