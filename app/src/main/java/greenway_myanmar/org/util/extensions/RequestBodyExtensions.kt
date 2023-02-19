package greenway_myanmar.org.util.extensions

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.math.BigDecimal

fun String?.toPlainTextRequestBody(): RequestBody {
  return this.orEmpty().toRequestBody("text/plain".toMediaType())
}

fun Long.toPlainTextRequestBody(): RequestBody {
  return this.toString().toPlainTextRequestBody()
}

fun BigDecimal.toPlainTextRequestBody(): RequestBody {
  return this.toPlainString().toPlainTextRequestBody()
}

fun Boolean.toPlainTextRequestBody(): RequestBody {
  return this.toString().toPlainTextRequestBody()
}

fun Uri?.fileName(contentResolver: ContentResolver): String {
  this?.let { uri -> contentResolver.query(uri, null, null, null, null) }?.use { cursor ->
    /*
     * Get the column indexes of the data in the Cursor,
     * move to the first row in the Cursor, get the data,
     * and display it.
     */
    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor.moveToFirst()
    return cursor.getString(nameIndex)
  }
  return ""
}
