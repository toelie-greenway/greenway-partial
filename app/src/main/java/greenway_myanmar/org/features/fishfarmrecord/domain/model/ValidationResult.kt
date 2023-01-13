package greenway_myanmar.org.features.fishfarmrecord.domain.model

import greenway_myanmar.org.common.domain.entities.Text

sealed class ValidationResult<out T>(val isSuccessful: Boolean) {
    data class Success<T>(val data: T) : ValidationResult<T>(true)
    data class Error(val message: Text? = null) : ValidationResult<Nothing>(false)
}

fun <T> ValidationResult<T>.getDataOrThrow() = if (this is ValidationResult.Success) {
    this.data
} else {
    throw IllegalStateException("ValidationResult, $this must be ValidationResult.Success")
}

fun <T> ValidationResult<T>.getErrorOrThrow() = if (this is ValidationResult.Error) {
    this.message
} else {
    throw IllegalStateException("ValidationResult, $this must be ValidationResult.Error")
}

fun <T> ValidationResult<T>.getErrorOrNull() = (this as? ValidationResult.Error)?.message