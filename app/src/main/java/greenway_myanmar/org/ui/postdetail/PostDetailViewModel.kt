package greenway_myanmar.org.ui.postdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.vo.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(

): ViewModel() {

    private val _post: MutableLiveData<Post?> = MutableLiveData();
    val post: LiveData<Post?> = _post

    init {
        _post.value = Post().apply {
            this.id = "1"
            this.audioFile = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3"
            this.youtubeIFrame = "<iframe id=\"ytplayer\" type=\"text/html\" width=\"640\" height=\"360\"\n" +
                    "  src=\"https://www.youtube.com/embed/M7lc1UVf-VE?autoplay=1&origin=http://example.com\"\n" +
                    "  frameborder=\"0\"></iframe>"
        }

        viewModelScope.launch {
            delay(10000)
            _post.value = Post().apply {
                this.id = "1"
                this.audioFile = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                this.youtubeIFrame = "<iframe id=\"ytplayer\" type=\"text/html\" width=\"640\" height=\"360\"\n" +
                        "  src=\"https://www.youtube.com/embed/M7lc1UVf-VE?autoplay=1&origin=http://example.com\"\n" +
                        "  frameborder=\"0\"></iframe>"
            }
        }
    }
}