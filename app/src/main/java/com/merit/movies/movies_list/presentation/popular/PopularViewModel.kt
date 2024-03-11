package com.merit.movies.movies_list.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merit.movies.movies_list.domain.repository.MovieListRepository
import com.merit.movies.movies_list.util.Category
import com.merit.movies.movies_list.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()


    init {
        getPopularMovieList()
    }

    fun onEvent(event: MovieListUiEvent) {
        when (event) {
            MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }
            }

            is MovieListUiEvent.Paginate -> {
                    getPopularMovieList()
            }
        }
    }

    private fun getPopularMovieList() {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {

                                it.copy(

                                    popularMovieList = movieListState.value.popularMovieList
                                            + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }
}