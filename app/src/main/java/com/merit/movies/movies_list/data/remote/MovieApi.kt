package com.merit.movies.movies_list.data.remote

import com.merit.movies.movies_list.data.remote.response.image.MovieImagesDto
import com.merit.movies.movies_list.data.remote.response.movie.MovieListDto
import com.merit.movies.movies_list.data.remote.response.video.MovieVideoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideo(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieVideoDto

    @GET("movie/{movieId}/images")
    suspend fun getMovieImages(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieImagesDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "52f8f7af02180e216534a74dbdc858b3"
    }

}