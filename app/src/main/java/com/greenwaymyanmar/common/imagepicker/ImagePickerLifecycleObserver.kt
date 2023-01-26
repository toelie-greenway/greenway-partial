package com.greenwaymyanmar.common.imagepicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.dhaval2404.imagepicker.ImagePicker
import timber.log.Timber

class ImagePickerLifecycleObserver(
  private val key: String,
  private val fragment: Fragment,
  private val registry: ActivityResultRegistry,
  private val imageResultListener: ImageResultListener
) : DefaultLifecycleObserver {

  var imageResultLauncher: ActivityResultLauncher<Intent>? = null

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    imageResultLauncher =
      registry.register(key, ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
          Activity.RESULT_OK -> {
            // Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!
            imageResultListener.onSuccess(fileUri)
          }
          ImagePicker.RESULT_ERROR -> {
            imageResultListener.onError(ImagePicker.getError(data))
            Toast.makeText(fragment.context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
          }
          else -> {
            imageResultListener.onCancel()
            Timber.d("Task Cancelled")
          }
        }
      }
  }

  fun selectImage() {
    imageResultLauncher?.let {
      ImagePicker.with(fragment).createIntent { intent -> imageResultLauncher?.launch(intent) }
    }
  }

  fun pickImage() {
    imageResultLauncher?.let {
      ImagePicker.with(fragment).galleryOnly().crop().createIntent { intent ->
        imageResultLauncher?.launch(intent)
      }
    }
  }

  fun takePicture() {
    imageResultLauncher?.let {
      ImagePicker.with(fragment).cameraOnly().crop().createIntent { intent ->
        imageResultLauncher?.launch(intent)
      }
    }
  }

  fun pickProfileImage() {
    imageResultLauncher?.let {
      ImagePicker.with(fragment).galleryOnly().cropSquare().createIntent { intent ->
        imageResultLauncher?.launch(intent)
      }
    }
  }

  fun selectProfileImage() {
    imageResultLauncher?.let {
      ImagePicker.with(fragment).cropSquare().createIntent { intent ->
        imageResultLauncher?.launch(intent)
      }
    }
  }

  fun takeProfilePicture() {
    imageResultLauncher?.let {
      ImagePicker.with(fragment).cameraOnly().cropSquare().createIntent { intent ->
        imageResultLauncher?.launch(intent)
      }
    }
  }

  interface ImageResultListener {
    fun onSuccess(uri: Uri)
    fun onError(error: String)
    fun onCancel()
  }
}
