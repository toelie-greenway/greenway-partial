package greenway_myanmar.org.features.fishfarmrecord.presentation.model

sealed class UiLoanDuration(open val month: Int) {
    object ThreeMonth : UiLoanDuration(3)
    object SixMonth : UiLoanDuration(6)
    object TwelveMonth : UiLoanDuration(12)
    data class Custom(override val month: Int) : UiLoanDuration(month)

    companion object {
//        fun fromViewId(@IdRes viewId: Int) = when (viewId) {
//            R.id.loan_duration_3_months_button -> ThreeMonth
//            R.id.loan_duration_6_months_button -> SixMonth
//            R.id.loan_duration_12_months_button -> TwelveMonth
//            R.id.loan_duration_custom_button -> Custom()
//        }
    }
}