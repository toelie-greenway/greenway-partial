package com.greenwaymyanmar.common.ui.epoxy.model

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.data.api.v2.interceptors.NoConnectivityException
import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.ListLoadingStateViewBinding
import timber.log.Timber

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ListLoadingStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding =
        ListLoadingStateViewBinding.inflate(LayoutInflater.from(context), this)
    var retryCallback: OnClickListener? = null
        @CallbackProp set

    private var _loadingState: LoadingState<*>? = null
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

    @ModelProp(ModelProp.Option.DoNotHash)
    fun setLoadingState( state: LoadingState<*>?) {
        _loadingState = state
    }

    @ModelProp
    fun setLoadingView(@LayoutRes layoutId: Int) {
        if (layoutId != -1 && layoutId != 0) {
            replaceLoadingView(layoutId)
        }
    }

    private fun bind(state: LoadingState<*>?) {
        binding.errorMessageTextView.text = if (state is LoadingState.Error) {
            state.exception.errorText().asString(context)
        } else {
            ""
        }
        binding.errorMessageTextView.isVisible = state is LoadingState.Error

        binding.loadingViewContainer.isVisible = state is LoadingState.Loading

        binding.retryButton.isVisible = state is LoadingState.Error
                && state.exception is NoConnectivityException
    }

    private fun replaceLoadingView(@LayoutRes layoutResId: Int) {
        try {
            binding.loadingViewContainer.removeAllViews()
            LayoutInflater.from(context).inflate(layoutResId, binding.loadingViewContainer, true)
        } catch (e: Resources.NotFoundException) {
            Timber.e("Invalid layout resource id: $layoutResId", e)
        }
    }
}