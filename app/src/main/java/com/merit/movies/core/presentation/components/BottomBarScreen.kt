package com.merit.movies.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Popular : BottomBarScreen(
        route = "popular",
        title = "Popular",
        icon = Icons.Default.Home
    )

    object Recent : BottomBarScreen(
        route = "recent",
        title = "Recent",
        icon = Icons.Default.Favorite
    )

    object TopRated : BottomBarScreen(
        route = "topRated",
        title = "TopRated",
        icon = Icons.Default.Brightness5
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )

    object MovieInfo: BottomBarScreen(
        route = "details",
        title = "Details",
        icon = Icons.Default.Details
    )
}