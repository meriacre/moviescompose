package com.merit.movies.movies_list.presentation.movieinfo

import com.merit.movies.movies_list.domain.model.Movie

data class RecommendedMovieListState(
    val isLoading: Boolean = false,
    val recommendedMovieListPage: Int = 1,
    val recommendedMovieList: List<Movie> = emptyList()
)