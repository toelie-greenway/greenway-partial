package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.GreenWayLoadingStateViewBinding
import greenway_myanmar.org.ui.common.RetryCallback

class GreenWayLoadingStateView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var _retryCallback: RetryCallback? = null

    private val binding = GreenWayLoadingStateViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun bind(state: LoadingState<*>) {
        isVisible = state !is LoadingState.Success
        bindEmptyView(state)
        bindErrorView(state)
        bindLoadingView(state)
    }

    private fun bindEmptyView(state: LoadingState<*>) {
        if (state is LoadingState.Empty) {
            binding.emptyMessageText.text =
                state.message?.asString(context) ?: resources.getString(R.string.empty_list)
        }
        binding.emptyMessageText.isVisible = state is LoadingState.Empty
    }

    private fun bindErrorView(state: LoadingState<*>) {
        if (state is LoadingState.Error) {
            binding.errorMessageTextView.text =
                state.message?.asString(context) ?: resources.getString(R.string.unknown_error)

            binding.retryButton.setOnClickListener {
                if (state.retryable) {
                    _retryCallback?.retry()
                }
            }
        }
        binding.retryButton.isVisible = state is LoadingState.Error && state.retryable
        binding.errorMessageTextView.isVisible = state is LoadingState.Error
    }

    private fun bindLoadingView(state: LoadingState<*>) {
        binding.loadingIndicator.isVisible = state is LoadingState.Loading
    }

    fun setRetryCallback(callback: RetryCallback) {
        _retryCallback = callback
    }

}

