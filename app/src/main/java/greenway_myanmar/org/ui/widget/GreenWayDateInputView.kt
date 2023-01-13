package greenway_myanmar.org.ui.widget

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import greenway_myanmar.org.databinding.GreenWayDateInputViewBinding
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.util.extensions.findBaseContext
import greenway_myanmar.org.util.extensions.toCalendar
import java.util.*

class GreenWayDateInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    LinearLayout(context, attrs) {

    private var _mandatory = false

    private var _minDate: Date? = null
    private var _maxDate: Date? = null
    private var selectedDate: Calendar = Calendar.getInstance()

    var binding: GreenWayDateInputViewBinding =
        GreenWayDateInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        updateDate(selectedDate)
        binding.root.setOnClickListener { showMaterialDesignDatePicker() }
        binding.increaseButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate.time
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            if (_maxDate != null) {
                if (!calendar.after(_maxDate?.toCalendar())) {
                    updateDate(calendar)
                }
            } else {
                updateDate(calendar)
            }
        }
        binding.decreaseButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate.time
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            if (_minDate != null) {
                if (!calendar.before(_minDate?.toCalendar())) {
                    updateDate(calendar)
                }
            } else {
                updateDate(calendar)
            }
        }
    }

    private fun showMaterialDesignDatePicker() {
        val activity: AppCompatActivity = context.findBaseContext() ?: return

        val now = selectedDate
        val datePicker = MaterialDatePicker.Builder.datePicker()
        datePicker.build().show(activity.supportFragmentManager, "DATE_PICKER_DIALOG")
//
//            DatePickerDialog.newInstance(
//                this,
//                now[Calendar.YEAR],
//                now[Calendar.MONTH],
//                now[Calendar.DAY_OF_MONTH]
//            )
//        _minDate?.let { dpd.minDate = it.toCalendar() }
//        _maxDate?.let { dpd.maxDate = it.toCalendar() }
//
//        dpd.accentColor = ContextCompat.getColor(context, R.color.theme_primary)
//        dpd.setOkColor(ContextCompat.getColor(context, R.color.theme_primary))
//        dpd.setCancelColor(ContextCompat.getColor(context, R.color.theme_primary))
//        dpd.show(activity.supportFragmentManager, "DATE_PICKER_DIALOG")
    }
//
//    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
//        val calendar = Calendar.getInstance()
//        calendar[Calendar.YEAR] = year
//        calendar[Calendar.MONTH] = monthOfYear
//        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
//
//        updateDate(calendar)
//    }

    private fun updateDate(calendar: Calendar) {
        selectedDate = calendar
        binding.dateTextView.text = getUserFriendlyDate(calendar)
    }

    fun setDate(date: Date) {
        updateDate(date.toCalendar())
    }

    fun setMinDate(date: Date) {
        _minDate = date
    }

    fun setMaxDate(date: Date) {
        _maxDate = date
    }

    private fun getUserFriendlyDate(calendar: Calendar): String {
        val sb = StringBuilder()
        if (DateUtils.isToday(calendar.timeInMillis)) {
            sb.append("ယနေ့ • ")
        }
        sb.append(MyanmarZarConverter.getUserFriendlyDateInMyanmar(calendar.time, true))
        return sb.toString()
    }
}
