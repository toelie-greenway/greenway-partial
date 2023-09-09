package greenway_myanmar.org.ui.postdetail.epoxy.models

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import greenway_myanmar.org.databinding.PostDetailAudioViewBinding
import greenway_myanmar.org.util.kotlin.customViewBinding
import timber.log.Timber

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PostDetailAudioView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    var lifecycleOwner: LifecycleOwner? = null
        @ModelProp(ModelProp.Option.DoNotHash) set(value) {
            if (field != null && field == value) {
                return
            }
            field = value

            value?.lifecycle?.addObserver(this)
        }

    var audioFileUrl: String? = null
        @ModelProp set(value) {
            Timber.d("audioFileUrl: old: $field, new: $value")
            if (field != null && field == value) {
                return
            }
            field = value

            releasePlayer()
            resetPosition = 0
            if (!value.isNullOrEmpty()) {
                initializePlayer()
            }
        }

    val binding = customViewBinding(PostDetailAudioViewBinding::inflate)

    private var player: ExoPlayer? = null
    private var resetPosition: Long = 0L
    private var playWhenReady: Boolean = false

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (audioFileUrl.isNullOrEmpty()) {
            return
        }

        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        binding.playerControlView.player = player
        val dataSourceFactory =
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, "GreenWay"),
                null
            )
        val mediaSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(audioFileUrl))
        player?.prepare(mediaSource)
        player?.seekTo(resetPosition)
        player?.playWhenReady = playWhenReady
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playWhenReady = exoPlayer.playWhenReady
            resetPosition = exoPlayer.currentPosition
            exoPlayer.release()
        }
        binding.playerControlView.player = null
        player = null
    }
}