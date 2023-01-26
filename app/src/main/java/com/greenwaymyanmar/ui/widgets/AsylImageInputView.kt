package com.greenwaymyanmar.ui.widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.greenwaymyanmar.dynamicviewparser.widget.SimpleInputWidget
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.AsylImageInputViewBinding

class AsylImageInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  LinearLayout(context, attrs, defStyleAttr), SimpleInputWidget {

  private lateinit var binding: AsylImageInputViewBinding

  private var _mandatory = false

  var emptyState = true

  var onImageInputListener: OnImageInputListener? = null

  var imageFilePath: String? = null
    set(value) {
      field = value
      this.emptyState = imageFilePath.isNullOrEmpty()
      showHideViews()
      value?.let { showPreview(it) }
      binding.imagePath = imageFilePath
      // clearError()
    }

  val imageUrl: String? = null

  init {
    inflateLayout()
    initViewListeners()
    showHideViews()
  }

  private fun inflateLayout() {
    binding = AsylImageInputViewBinding.inflate(LayoutInflater.from(context), this, true)
  }

  private fun initViewListeners() {
    binding.takePhotoButton.setOnClickListener { onImageInputListener?.takePhoto(this) }
    binding.pickImageButton.setOnClickListener { onImageInputListener?.pickImage(this) }
  }

  fun showErrorView() {
    // pickImageTextInputLayout.setError(context.resources.getString(R.string.error_photo_required))
  }

  fun clearErrorView() {
    // pickImageTextInputLayout.setError(null)
    // pickImageTextInputLayout.setErrorEnabled(false)
  }

  fun setHint(hint: String?) {
    // pickImageTextInputLayout.setHint(hint)
    binding.titleTextView.text = hint
  }

  fun setHint(hintResId: Int) {
    // pickImageTextInputLayout.setHint(resources.getString(hintResId))
    binding.titleTextView.setText(hintResId)
  }

  fun showHideViews() {
    if (emptyState) {
      binding.previewImageView.visibility = GONE
      // contentGroup.setVisibility(GONE)
    } else {
      // .setVisibility(VISIBLE)
      binding.previewImageView.visibility = VISIBLE
    }
  }

  fun showPreview(imageFilePath: String) {
    Glide.with(binding.previewImageView.context).load(imageFilePath).into(binding.previewImageView)
  }

  fun clear() {
    this.imageFilePath = ""
    this.emptyState = true
    binding.previewImageView.setImageDrawable(null)
    showHideViews()
  }

  override fun getValue(): String {
    return imageFilePath ?: ""
  }

  override fun setMandatory(mandatory: Boolean) {
    _mandatory = mandatory
  }

  override fun isMandatory(): Boolean = _mandatory

  override fun validate(): Boolean {
    return if (_mandatory && isEmpty()) {
      showError()
      false
    } else {
      clearError()
      true
    }
  }

  override fun isEmpty(): Boolean {
    return TextUtils.isEmpty(imageFilePath)
  }

  override fun showError() {
    showErrorView()
  }

  override fun clearError() {
    clearErrorView()
  }

  fun loadImageFromUrl(imageUrl: String?) {
    val glide = Glide.with(binding.previewImageView.context)
    glide
      .load(imageUrl)
      .placeholder(R.drawable.sample_square_shape)
      .animate(R.anim.image_fade_in)
      .into(binding.previewImageView)
  }

  interface OnImageInputListener {
    fun pickImage(view: AsylImageInputView)
    fun takePhoto(view: AsylImageInputView)
    fun removePhoto(view: AsylImageInputView)
  }
}
