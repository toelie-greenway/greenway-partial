package greenway_myanmar.org.util.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController


// credit goes to https://stackoverflow.com/a/63136873
fun <T> Fragment.setNavigationResult(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(
        key,
        value
    )
}

fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T) -> Unit) {
    val navBackStackEntry = findNavController().getBackStackEntry(id)

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME
            && navBackStackEntry.savedStateHandle.contains(key)
        ) {
            val result = navBackStackEntry.savedStateHandle.get<T>(key)
            result?.let(onResult)
            navBackStackEntry.savedStateHandle.remove<T>(key)
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}