package com.merit.movies.movies_list.domain.repository

import com.merit.movies.movies_list.data.remote.response.image.MovieImagesDto
import com.merit.movies.movies_list.data.remote.response.video.MovieVideo
import com.merit.movies.movies_list.domain.model.Movie
import com.merit.movies.movies_list.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovieVideo(id: Int): Flow<Resource<MovieVideo>>

    suspend fun getMovieImages(id: Int): Flow<Resource<MovieImagesDto>>

    suspend fun getMovieRecommendations(id: Int, page: Int): Flow<Resource<List<Movie>>>
}