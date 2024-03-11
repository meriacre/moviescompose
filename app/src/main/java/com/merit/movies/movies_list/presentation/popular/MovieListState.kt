package com.merit.movies.movies_list.presentation.popular

import com.merit.movies.movies_list.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,
    val topMovieListPage: Int = 1,

    val isCurrentPopularScreen: Boolean = true,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList(),
    val topMovieList: List<Movie> = emptyList()
)