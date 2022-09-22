package greenway_myanmar.org.features.farmingrecord.qr.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.PhoneInputDialogBinding
import greenway_myanmar.org.util.kotlin.autoCleared

class PhoneInputDialog : DialogFragment(R.layout.phone_input_dialog) {

    private var binding: PhoneInputDialogBinding by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_GreenWay_PhoneInputDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PhoneInputDialogBinding.bind(view)

        binding.confirmButton.setOnClickListener {
            onSubmit()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun onSubmit() {
        binding.phoneInputLayout.error = null
        val phone = binding.phoneInputEditText.text.toString().trim()
        if (phone.isEmpty()) {
            binding.phoneInputLayout.error = resources.getString(R.string.error_field_required)
            return
        }

        setFragmentResult(
            REQUEST_KEY_PHONE, bundleOf(
                EXTRA_PHONE to phone
            )
        )
        dismiss()
    }

    companion object {
        const val REQUEST_KEY_PHONE = "keys.Phone"
        const val EXTRA_PHONE = "extras.Phone"
        fun newInstance() = PhoneInputDialog()
    }
}