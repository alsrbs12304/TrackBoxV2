package com.mgpark.trackbox.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data object AddRoute

@Serializable
data class DetailRoute(val id: Long)
