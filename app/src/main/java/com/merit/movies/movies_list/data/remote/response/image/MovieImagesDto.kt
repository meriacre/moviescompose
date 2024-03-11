package com.merit.movies.movies_list.data.remote.response.image

data class MovieImagesDto(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
)