package greenway_myanmar.org.ui.postdetail.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import greenway_myanmar.org.databinding.PostDetailVideoViewBinding
import greenway_myanmar.org.util.kotlin.customViewBinding
import timber.log.Timber

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PostDetailVideoView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    val binding = customViewBinding(PostDetailVideoViewBinding::inflate)

    var lifecycleOwner: LifecycleOwner? = null
        @ModelProp(ModelProp.Option.DoNotHash) set(value) {
            if (field != null && field == value) {
                return
            }
            field = value
        }

    var youtubeVideoUrl: String? = null
        @ModelProp set(value) {
            Timber.d("audioFileUrl: old: $field, new: $value")
            if (field != null && field == value) {
                return
            }
            field = value

            if (!value.isNullOrEmpty()) {
                initializePlayer()
            }
        }

    private fun initializePlayer() {
        val youtubePlayer = binding.youtubePlayerView
        val videoId = "S0Q4gqBUs7c"
        lifecycleOwner?.lifecycle?.removeObserver(binding.youtubePlayerView)
        lifecycleOwner?.lifecycle?.addObserver(binding.youtubePlayerView)
        youtubePlayer.initialize(
            { youTubePlayer: YouTubePlayer ->
                youTubePlayer.addListener(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady() {
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    }
                )
            },
            true
        )
    }
//
//
//    private var player: ExoPlayer? = null
//    private var resetPosition: Long = 0L
//    private var playWhenReady: Boolean = false
//
//    private fun initializePlayer() {
//        if (audioFileUrl.isNullOrEmpty()) {
//            return
//        }
//
//        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
//        binding.playerControlView.player = player
//        val dataSourceFactory =
//            DefaultDataSourceFactory(
//                context,
//                Util.getUserAgent(context, "GreenWay"),
//                null
//            )
//        val mediaSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(Uri.parse(audioFileUrl))
//        player?.prepare(mediaSource)
//        player?.seekTo(resetPosition)
//        player?.playWhenReady = playWhenReady
//    }
//
//    private fun releasePlayer() {
//        player?.let { exoPlayer ->
//            playWhenReady = exoPlayer.playWhenReady
//            resetPosition = exoPlayer.currentPosition
//            exoPlayer.release()
//        }
//        binding.playerControlView.player = null
//        player = null
//    }
}