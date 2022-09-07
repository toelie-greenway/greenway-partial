package greenway_myanmar.org.common.domain.entities

sealed class ValidationResult {
    object Success: ValidationResult()
    data class Error(val message: Text): ValidationResult()
}

fun ValidationResult.errorMessage(): Text? {
    return if (this is ValidationResult.Error) {
        this.message
    } else {
        null
    }
}