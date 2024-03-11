package com.merit.movies.movies_list.presentation.movieinfo

import com.merit.movies.movies_list.data.remote.response.video.MovieVideo

data class MovieVideosState(
    val isLoading: Boolean = false,
    val movie: MovieVideo? = null,
    val showVideoDialog: Boolean = false,
    var videoTime: Float = 0f
)