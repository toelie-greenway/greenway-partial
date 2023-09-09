package greenway_myanmar.org.ui.postdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.PostDetailFragmentBinding
import greenway_myanmar.org.ui.postdetail.epoxy.controller.PostDetailController
import greenway_myanmar.org.util.kotlin.viewBinding
import timber.log.Timber


@AndroidEntryPoint
class PostDetailFragment : Fragment(R.layout.post_detail_fragment) {

    private val binding by viewBinding(PostDetailFragmentBinding::bind)

    private val viewModel: PostDetailViewModel by viewModels()

    private val controller by lazy {
        PostDetailController(
            lifecycleOwner = viewLifecycleOwner
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupYouTubePlayer()
        setupList()
    }

    private fun setupYouTubePlayer() {
        val youtubePlayer = binding.youtubePlayerView
        val videoId = "S0Q4gqBUs7c"
        lifecycle.addObserver(youtubePlayer)
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

    private fun setupList() {
        binding.list.setController(controller)
    }

    private fun observeViewModel() {
        viewModel.post.observe(viewLifecycleOwner) {
            Timber.d("Post: ${it?.audioFile}")
            controller.post = it
        }
    }
}