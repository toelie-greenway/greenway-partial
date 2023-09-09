package greenway_myanmar.org.common.domain.entities


sealed class ValidationResult<out T>(val isSuccessful: Boolean) {
  data class Success<T>(val data: T) : ValidationResult<T>(true)
  data class Error<T>(val message: Text? = null, val data: T? = null) : ValidationResult<T>(false)
}

fun <T> ValidationResult<T>.getDataOrThrow() =
  if (this is ValidationResult.Success) {
    this.data
  } else {
    throw IllegalStateException("ValidationResult, $this must be ValidationResult.Success")
  }

fun <T> ValidationResult<T>.getDataOrNull() =
  if (this is ValidationResult.Success) {
    this.data
  } else {
    null
  }

fun <T> ValidationResult<T>.getDataOrDefault(default: T) =
  if (this is ValidationResult.Success) {
    this.data
  } else {
    default
  }

fun <T> ValidationResult<T>.getErrorOrThrow() =
  if (this is ValidationResult.Error) {
    this.message
  } else {
    throw IllegalStateException("ValidationResult, $this must be ValidationResult.Error")
  }

fun <T> ValidationResult<T>.getErrorOrNull() = (this as? ValidationResult.Error)?.message

fun <T> ValidationResult<T>.isError() = (this is ValidationResult.Error)

fun hasError(vararg validationResults: ValidationResult<Any?>): Boolean {
  return validationResults.toList().any { !it.isSuccessful }
}
