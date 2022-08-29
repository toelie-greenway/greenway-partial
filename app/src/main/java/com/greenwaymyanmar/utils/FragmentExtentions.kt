package com.greenwaymyanmar.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.setSupportActionBar(toolbar: Toolbar) {
  (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
}

/**
 * Launches a new coroutine and repeats `block` every time the Fragment's viewLifecycleOwner is in
 * and out of `minActiveState` lifecycle state.
 */
inline fun Fragment.launchAndRepeatWithViewLifecycle(
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  crossinline block: suspend CoroutineScope.() -> Unit
) {
  viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) { block() }
  }
}