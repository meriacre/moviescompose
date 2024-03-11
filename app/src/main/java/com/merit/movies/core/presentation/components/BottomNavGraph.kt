package com.merit.movies.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.merit.movies.movies_list.presentation.movieinfo.MovieScreen
import com.merit.movies.movies_list.presentation.popular.PopularScreen
import com.merit.movies.movies_list.presentation.recent.RecentScreen
import com.merit.movies.movies_list.presentation.toprated.TopRatedScreen
import com.merit.movies.settings.presentation.SettingsScreen

@Composable
fun BottomNavGraph(navHostController:  NavHostController){
    NavHost(
        navController = navHostController,
        startDestination = BottomBarScreen.Popular.route
    ) {
        composable(route = BottomBarScreen.Popular.route) {
            PopularScreen(navHostController)
        }
        composable(route = BottomBarScreen.Recent.route) {
            RecentScreen(navHostController)
        }
        composable(route = BottomBarScreen.TopRated.route) {
            TopRatedScreen(navHostController)
        }
        composable(route = BottomBarScreen.MovieInfo.route){
            MovieScreen(navHostController)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
            }
    }
}