package com.merit.movies.movies_list.presentation.movieinfo

import com.merit.movies.movies_list.data.remote.response.image.Backdrop

data class MovieImagesState(
    val isLoading: Boolean = false,
    val imageBackdropList: List<Backdrop>? = null
)