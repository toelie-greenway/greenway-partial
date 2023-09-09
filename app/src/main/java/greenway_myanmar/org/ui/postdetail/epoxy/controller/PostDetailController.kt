package greenway_myanmar.org.ui.postdetail.epoxy.controller

import androidx.lifecycle.LifecycleOwner
import com.airbnb.epoxy.EpoxyController
import greenway_myanmar.org.ui.postdetail.epoxy.models.PostDetailAudioViewModel_
import greenway_myanmar.org.ui.postdetail.epoxy.models.PostDetailVideoViewModel_
import greenway_myanmar.org.util.IFrameUrlParser
import greenway_myanmar.org.vo.Post

class PostDetailController constructor(
    private val lifecycleOwner: LifecycleOwner,
) : EpoxyController() {

    var post: Post? = null
        set(value) {
            if (field != null && field == value) {
                return
            }
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        post?.let { post ->
            val audioFileUrl = post.audioFile
            if (!audioFileUrl.isNullOrEmpty()) {
                PostDetailAudioViewModel_()
                    .id("audio-view")
                    .lifecycleOwner(lifecycleOwner)
                    .audioFileUrl(audioFileUrl)
                    .addTo(this)
            }

            val youtubeIFrame = post.youtubeIFrame
            if (!youtubeIFrame.isNullOrEmpty()) {
                val youtubeUrl = IFrameUrlParser.chunkYoutubeUrl(youtubeIFrame)
                val videoId = youtubeUrl.replace("https://www.youtube.com/embed/", "")
                if (!audioFileUrl.isNullOrEmpty()) {
                    PostDetailVideoViewModel_()
                        .id("video-view")
                        .lifecycleOwner(lifecycleOwner)
                        .youtubeVideoUrl(videoId)
                        .addTo(this)
                }
            }
        }
    }
}