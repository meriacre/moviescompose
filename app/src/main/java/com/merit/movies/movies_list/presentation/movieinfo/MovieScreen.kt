package com.merit.movies.movies_list.presentation.movieinfo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.merit.movies.R
import com.merit.movies.movies_list.data.remote.MovieApi
import com.merit.movies.movies_list.domain.model.Movie
import com.merit.movies.movies_list.presentation.components.ImageItem
import com.merit.movies.movies_list.presentation.components.MovieItem
import com.merit.movies.movies_list.presentation.popular.MovieListUiEvent
import com.merit.movies.movies_list.util.Category
import com.merit.movies.movies_list.util.RatingBar

@Composable
fun MovieScreen(navHostController: NavHostController) {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val movieVideosState = detailsViewModel.movieVideoState.collectAsState().value
    val movieImagesState = detailsViewModel.movieImagesState.collectAsState().value
    val movieState = detailsViewModel.detailsState.collectAsState().value
    val movieRecommendedMovieListState = detailsViewModel.movieRecommendedListState.collectAsState().value
//    detailsViewModel.detailsState.value.movie.
//    var showDialog by remember { mutableStateOf(false) }


    val movie = navHostController.previousBackStackEntry?.savedStateHandle?.get<Movie>(key = "movie")
    if (movie != null) {
        detailsViewModel.setDetailsState(movie)
    }

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movieState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movieState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    if (movieVideosState.showVideoDialog) {
        BackHandler {
           detailsViewModel.setMovieVideoState(false)
        }

        Dialog(
            onDismissRequest = { detailsViewModel.setMovieVideoState(false) },
                    properties = DialogProperties(
                    usePlatformDefaultWidth = false
                    )
        ) {
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = 8.dp,
                border = BorderStroke(4.dp, Color.LightGray)
            ) {
               YoutubeView(
                   youtubeVideoId = movieVideosState.movie?.key ?: "1234",
                   lifecycleOwner = LocalLifecycleOwner.current,
                   savedTime = movieVideosState.videoTime,
                   saveTimePlayback = {detailsViewModel.setYTVideoTime(it)

               })
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (backDropImageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = movieState.movie?.title
                )
            }
        }

        if (backDropImageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                painter = backDropImageState.painter,
                contentDescription = movieState.movie?.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
            ) {
                if (posterImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = movieState.movie?.title
                        )
                    }
                }

                if (posterImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = movieState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            movieState.movie?.let { movie ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        RatingBar(
                            starsModifier = Modifier.size(18.dp),
                            rating = movie.vote_average / 2
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.language) + movie.original_language
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.release_date) + movie.release_date
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.vote_count.toString() + stringResource(R.string.votes)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
//                                Log.d("test", "testapasare")
                                detailsViewModel.setMovieVideoState(true)
                            },
                        text = stringResource(R.string.view_trailer),
                        color = Color.Blue,
                        fontSize = 18.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.overview),
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        movieState.movie?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                text = it.overview,
                fontSize = 14.sp,
                maxLines = Int.MAX_VALUE,
                overflow = TextOverflow.Clip

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.pics_movie),
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (movieImagesState.imageBackdropList.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box( modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 4.dp, horizontal = 6.dp)
                ) {
                    movieImagesState.imageBackdropList.let {
                        items(it.size) { index ->
                            ImageItem(url = it[index].file_path)
                            Spacer(modifier = Modifier.height(16.dp))

                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.recommendations),
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // here will be horizontal list with similar movies
        if (movieRecommendedMovieListState.recommendedMovieList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box( modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(360.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    items(movieRecommendedMovieListState.recommendedMovieList.size) { index ->
                        MovieItem(
                            movie = movieRecommendedMovieListState.recommendedMovieList[index],
                            navHostController = navHostController
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (index >= movieRecommendedMovieListState.recommendedMovieList.size - 1 && !movieRecommendedMovieListState.isLoading) {
                            detailsViewModel.onEvent(DetailUiEvent.Paginate)
                        }

                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(56.dp))
    }

}
