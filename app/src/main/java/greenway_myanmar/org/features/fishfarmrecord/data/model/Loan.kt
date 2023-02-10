package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrLoanEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkLoan
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero

fun NetworkLoan.asEntity() = FfrLoanEntity(
    amount = amount.toBigDecimalOrZero(),
    duration = duration ?: 0,
    organization = landing_organization.orEmpty(),
    remark = remark
)