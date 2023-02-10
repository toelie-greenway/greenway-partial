package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Loan
import kotlinx.serialization.Serializable

@Serializable
data class NetworkLoanRequest(
    val amount: Double,
    val duration: Int,
    val landing_organization: String,
    val remark: String?
) {
    companion object {
        fun fromDomainModel(domainModel: Loan) = NetworkLoanRequest(
            amount = domainModel.amount.toDouble(),
            duration = domainModel.duration,
            landing_organization = domainModel.organization,
            remark = domainModel.remark
        )
    }
}