package com.merit.movies.movies_list.presentation.toprated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merit.movies.movies_list.domain.repository.MovieListRepository
import com.merit.movies.movies_list.presentation.popular.MovieListState
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
class TopRatedViewModel@Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getTopMovieList()
    }

    fun paginate() {
        getTopMovieList()
    }

    private fun getTopMovieList() {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                Category.TOP,
                movieListState.value.topMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.topMovieList
                                            + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.topMovieListPage + 1
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