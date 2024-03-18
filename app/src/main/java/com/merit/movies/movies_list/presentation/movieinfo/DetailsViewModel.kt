package com.merit.movies.movies_list.presentation.movieinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merit.movies.movies_list.domain.model.Movie
import com.merit.movies.movies_list.domain.repository.MovieListRepository
import com.merit.movies.movies_list.presentation.popular.MovieListUiEvent
import com.merit.movies.movies_list.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {

    private var _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    private var _movieVideoState = MutableStateFlow(MovieVideosState())
    val movieVideoState = _movieVideoState.asStateFlow()

    private var _movieImagesState = MutableStateFlow(MovieImagesState())
    val movieImagesState = _movieImagesState.asStateFlow()

    private var _movieRecommendedState = MutableStateFlow(RecommendedMovieListState())
    val movieRecommendedListState = _movieRecommendedState.asStateFlow()

    fun setDetailsState(movie: Movie) {
        if (detailsState.value.movie == null) {
            _detailsState.update {
                it.copy(
                    isLoading = false,
                    movie = movie
                )
            }
            getMovie()
            getMoviePictures()
            getMovieRecommendation()
        }

    }

    fun setMovieVideoState(showVideoDialog: Boolean){
        _movieVideoState.update {
            it.copy(
                showVideoDialog = showVideoDialog
            )
        }
    }

    fun setYTVideoTime(time: Float){
        _movieVideoState.update {
            it.copy(
                videoTime = time
            )
        }
    }

    fun onEvent(event: DetailUiEvent) {
        when (event) {
            is DetailUiEvent.Paginate -> {
                if (movieRecommendedListState.value.recommendedMovieList)
                getMovieRecommendation()
            }
        }
    }

    private fun getMovie() {
        viewModelScope.launch {
            _movieVideoState.update {
                it.copy(isLoading = true)
            }

            detailsState.value.movie?.let {
                movieListRepository.getMovieVideo(it.id).collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _movieVideoState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }

                        is Resource.Loading -> {
                            _movieVideoState.update { state ->
                                state.copy(isLoading = result.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { movie ->
                                _movieVideoState.update { state->
                                    state.copy(movie = movie)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getMoviePictures(){
        viewModelScope.launch {
            _movieImagesState.update {
                it.copy(isLoading = true)
            }
            detailsState.value.movie?.let {
            movieListRepository.getMovieImages(it.id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieImagesState.update { state ->
                            state.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieImagesState.update { state ->
                            state.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieImagesState.update { state->
                                state.copy(imageBackdropList = movie.backdrops)
                            }
                        }
                    }
                }
            }
        }

    }
}

    private fun getMovieRecommendation() {
        viewModelScope.launch {

            _movieRecommendedState.update {
                it.copy(isLoading = true)
            }

            detailsState.value.movie?.let {
                movieListRepository.getMovieRecommendations(it.id, movieRecommendedListState.value.recommendedMovieListPage).collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _movieRecommendedState.update {
                                it.copy(isLoading = false)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { popularList ->
                                _movieRecommendedState.update {

                                    it.copy(

                                        recommendedMovieList = movieRecommendedListState.value.recommendedMovieList
                                                + popularList.shuffled(),
                                        recommendedMovieListPage = movieRecommendedListState.value.recommendedMovieListPage + 1
                                    )
                                }
                            }
                        }

                        is Resource.Loading -> {
                            _movieRecommendedState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                    }
                }
            }
        }
    }
}