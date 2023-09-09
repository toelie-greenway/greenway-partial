package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.GreenWayDateInputViewBinding
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.util.extensions.findBaseContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class GreenWayDateInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    FrameLayout(context, attrs) {

    private var _minDate: LocalDate? = null
    private var _maxDate: LocalDate? = null
    private var selectedDate: LocalDate = LocalDate.now()

    var onDateChangeListener: OnDateChangeListener? = null

    var binding: GreenWayDateInputViewBinding =
        GreenWayDateInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        updateDate(selectedDate)
        binding.root.setOnClickListener { showMaterialDesignDatePicker() }
        binding.increaseButton.setOnClickListener {
            increaseDate()
        }
        binding.decreaseButton.setOnClickListener {
            decreaseDate()
        }
    }

    private fun showMaterialDesignDatePicker() {
        val activity: AppCompatActivity = context.findBaseContext() ?: return

        val now = selectedDate
        val listValidators = ArrayList<DateValidator>()
        _minDate?.let {
            val dateValidatorMin: DateValidator = DateValidatorPointForward.from(it.toEpochMilli())
            listValidators.add(dateValidatorMin)
        }
        _maxDate?.let {
            val dateValidatorMax: DateValidator =
                DateValidatorPointBackward.before(it.toEpochMilli())
            listValidators.add(dateValidatorMax)
        }
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val validators = CompositeDateValidator.allOf(listValidators)
        constraintsBuilderRange.setValidator(validators)
        val calendarConstraints = constraintsBuilderRange.build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(now.toEpochMilli())
            .setCalendarConstraints(calendarConstraints)
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            onDateSet(selection)
        }
        datePicker.show(activity.supportFragmentManager, "DELIVERY_DATE_PICKER_DIALOG")
    }

    private fun onDateSet(epochMilli: Long) {
        updateDate(epochMilli.toLocalDate())
    }

    private fun increaseDate() {
        val newDate = selectedDate.plusDays(1)
        if (_maxDate != null) {
            if (newDate <= _maxDate) {
                updateDate(newDate)
            }
        } else {
            updateDate(newDate)
        }
    }

    private fun decreaseDate() {
        val newDate = selectedDate.minusDays(1)
        if (_minDate != null) {
            if (newDate >= _minDate) {
                updateDate(newDate)
            }
        } else {
            updateDate(newDate)
        }
    }

    private fun updateDate(date: LocalDate) {
        selectedDate = date
        binding.dateTextView.text = getUserFriendlyDate(date)
        onDateChangeListener?.onDateChanged(date)
    }

    fun setDate(date: LocalDate) {
        if (this.selectedDate == date) return

        updateDate(date)
    }

    fun setMinDate(date: LocalDate) {
        _minDate = date
    }

    fun setMaxDate(date: LocalDate) {
        _maxDate = date
    }

    private fun getUserFriendlyDate(date: LocalDate): String {
        val userFriendlyDate =
            MyanmarZarConverter.getUserFriendlyDateInMyanmar(date.toEpochMilli(), true)
        return if (isToday(date)) {
            resources.getString(R.string.label_today_user_friendly_formatted_date, userFriendlyDate)
        } else {
            userFriendlyDate
        }
    }

    private fun isToday(date: LocalDate) =
        date == LocalDate.now()

    private fun LocalDate.toEpochMilli() =
        this.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    private fun Long.toLocalDate() =
        Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

    fun bindDate(date: LocalDate) {
        if (selectedDate == date) return

        updateDate(date)
    }

    interface OnDateChangeListener {
        fun onDateChanged(date: LocalDate)
    }
}
