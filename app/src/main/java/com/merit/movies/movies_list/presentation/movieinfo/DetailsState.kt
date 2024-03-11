package com.merit.movies.movies_list.presentation.movieinfo

import com.merit.movies.movies_list.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)