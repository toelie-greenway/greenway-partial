package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.models

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.databinding.TagThreadNetworkStateItemViewBinding
import greenway_myanmar.org.util.CommonConstants
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Status

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TagThreadNetworkStateItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding =
        TagThreadNetworkStateItemViewBinding.inflate(LayoutInflater.from(context), this)
    var retryCallback: OnClickListener? = null
        @CallbackProp set

    private var _networkState: NetworkState? = null
        set(value) {
            if (value != field) {
                field = value
                bind(value)
            }
        }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL

        binding.retryButton.setOnClickListener {
            retryCallback?.onClick(it)
        }
    }

    @ModelProp
    fun setNetworkState(state: NetworkState?) {
        _networkState = state
    }

    private fun bind(state: NetworkState?) {
        binding.errorMessageTextView.text = state?.msg.orEmpty()
        binding.errorMessageTextView.isVisible = !state?.msg.isNullOrEmpty()

        // binding.loadingIndicator.isVisible = state == NetworkState.LOADING

        binding.retryButton.isVisible = state?.status == Status.ERROR
                && state.msg == CommonConstants.NETWORK_ERROR_MESSAGE
    }
}