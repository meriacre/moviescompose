package com.merit.movies.movies_list.presentation.movieinfo

sealed class DetailUiEvent {
    data object Paginate : DetailUiEvent()
}