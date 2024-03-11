package com.merit.movies.movies_list.data.mappers

import com.merit.movies.movies_list.data.remote.response.movie.MovieDto
import com.merit.movies.movies_list.domain.model.Movie

fun MovieDto.toMovie(category: String): Movie {
    return Movie(
        backdrop_path = backdrop_path ?: "N/A",
        original_language = original_language ?: "N/A",
        overview = overview ?: "N/A",
        poster_path = poster_path ?: "N/A",
        release_date = release_date ?: "N/A",
        title = title ?: "N/A",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        video = video ?: false,
        id = id ?: 0,
        adult = adult ?: false,
        original_title = original_title ?: "N/A",

        category = category,

        genre_ids = genre_ids ?: listOf(-1, -2)
    )

}