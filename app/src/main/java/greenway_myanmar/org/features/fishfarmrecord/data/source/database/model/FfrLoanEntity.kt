package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkLoanRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Loan
import java.math.BigDecimal

data class FfrLoanEntity(
    val amount: BigDecimal,
    val duration: Int,
    val organization: String,
    val remark: String? = null
) {
    companion object {
        fun fromDomainModel(domainModel: Loan) = FfrLoanEntity(
            amount = domainModel.amount,
            duration = domainModel.duration,
            organization = domainModel.organization,
            remark = domainModel.remark
        )
    }
}

fun FfrLoanEntity.asDomainModel() = Loan(
    amount = amount,
    duration = duration,
    organization = organization,
    remark = remark
)

fun FfrLoanEntity.asNetworkModel() = NetworkLoanRequest(
    amount = amount.toDouble(),
    duration = duration,
    landing_organization = organization,
    remark = remark
)