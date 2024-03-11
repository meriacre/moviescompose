package com.merit.movies.movies_list.presentation.movieinfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubeView(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner,
    savedTime: Float,
    saveTimePlayback:(time: Float) -> Unit
){
    
    AndroidView(
        modifier = Modifier.padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth(),
        factory = { context ->
        YouTubePlayerView(context=context).apply {
            lifecycleOwner.lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    saveTimePlayback(second)
                    super.onCurrentSecond(youTubePlayer, second)
                }

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    if (savedTime > 0){
                        youTubePlayer.loadVideo(youtubeVideoId, savedTime)
                        saveTimePlayback(savedTime)
                    }else{
                    youTubePlayer.loadVideo(youtubeVideoId, 0f)
                    }
                }
            })

        }
    })

}