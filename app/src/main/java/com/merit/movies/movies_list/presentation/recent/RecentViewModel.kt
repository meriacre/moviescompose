package com.merit.movies.movies_list.presentation.recent

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
class RecentViewModel @Inject constructor(
                      private val movieListRepository: MovieListRepository
): ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getRecentMovieList()
    }

    fun paginate() {
        getRecentMovieList()
    }

    private fun getRecentMovieList() {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
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
                                    upcomingMovieList = movieListState.value.upcomingMovieList
                                            + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
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