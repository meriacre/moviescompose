package com.merit.movies.movies_list.data.remote.response.movie

data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)