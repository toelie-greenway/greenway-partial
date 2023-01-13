package greenway_myanmar.org.features.fishfarmrecord.domain.usecase.validation

import com.greenwaymyanmar.core.domain.usecase.ValidationUseCase
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import javax.inject.Inject

class PondNameValidationUseCase @Inject constructor() : ValidationUseCase<String?, String>() {
    override fun execute(params: String?): ValidationResult<String> {
        return if (params.isNullOrEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(params)
        }
    }
}