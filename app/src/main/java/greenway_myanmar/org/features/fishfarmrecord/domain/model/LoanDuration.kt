package greenway_myanmar.org.features.fishfarmrecord.domain.model

sealed class LoanDuration(open val month: Int) {
    object ThreeMonth : LoanDuration(3)
    object SixMonth : LoanDuration(6)
    object TwelveMonth : LoanDuration(12)
    data class Custom(override val month: Int) : LoanDuration(month)
}