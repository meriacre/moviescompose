package com.merit.movies.movies_list.data.repository

import com.merit.movies.movies_list.data.mappers.toMovie
import com.merit.movies.movies_list.data.remote.MovieApi
import com.merit.movies.movies_list.data.remote.response.image.MovieImagesDto
import com.merit.movies.movies_list.data.remote.response.video.MovieVideo
import com.merit.movies.movies_list.domain.model.Movie
import com.merit.movies.movies_list.domain.repository.MovieListRepository
import com.merit.movies.movies_list.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieListRepository {

    override suspend fun getMovieList(
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            emit(Resource.Success(
                movieListFromApi.results.let { it.map{
                    it.toMovie(category)
                } }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieVideo(id: Int): Flow<Resource<MovieVideo>> {
        return flow {
            emit(Resource.Loading(true))
            val movieVideoListFromApi = try {
                movieApi.getMovieVideo(id)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val trailer = movieVideoListFromApi.results.find {
                it.type == "Trailer" && it.site == "YouTube" }

            if (trailer != null){
                emit(Resource.Success(trailer))
            }else{
                if (movieVideoListFromApi.results.isNotEmpty()){
                emit(Resource.Success(movieVideoListFromApi.results[0]))
                }
            }
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieImages(id: Int): Flow<Resource<MovieImagesDto>> {
        return flow {
            emit(Resource.Loading(true))

         val movieImagesListFromApi = try {
             movieApi.getMovieImages(id)
         }catch (e: IOException) {
             e.printStackTrace()
             emit(Resource.Error(message = "Error loading movies"))
             return@flow
         } catch (e: HttpException) {
             e.printStackTrace()
             emit(Resource.Error(message = "Error loading movies"))
             return@flow
         } catch (e: Exception) {
             e.printStackTrace()
             emit(Resource.Error(message = "Error loading movies"))
             return@flow
         }

            emit(Resource.Success(movieImagesListFromApi))
            emit(Resource.Loading(false))

        }

    }

    override suspend fun getMovieRecommendations(id: Int, page: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val movieListFromApi = try {
                movieApi.getMovieRecommendations(id, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            emit(Resource.Success(
                movieListFromApi.results.let { it.map{
                    it.toMovie("recommendation")
                } }
            ))
            emit(Resource.Loading(false))
        }
    }


}