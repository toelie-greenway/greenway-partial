package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import javax.inject.Inject

class GenerateNewQrUseCase @Inject constructor(

) {
    suspend operator fun invoke(): GenerateNewQrResult {
        return GenerateNewQrResult.Success(
            qrNumber = "GW-22133",
            qrUrl = "https://greenwaymyanmar.com/content-providers/UNESCO-LIFT"
        )
    }

    sealed class GenerateNewQrResult {
        data class Success(val qrNumber: String, val qrUrl: String) : GenerateNewQrResult()
        data class Error(val message: String): GenerateNewQrResult()
    }
}