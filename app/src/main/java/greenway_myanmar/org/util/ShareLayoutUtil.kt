package greenway_myanmar.org.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ShareLayoutUtil {

    fun share(context: Context, view: View) {
        share4(context, view)
    }

    fun share1(view: View) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val bitmap = Bitmap.createBitmap(
            view.width,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        view.draw(Canvas(bitmap))

        try {
            val cacheDir: File = view.context.cacheDir
            val imageFile = File(cacheDir, "share_image.png")
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun share2(context: Context, view: View) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val bitmap = Bitmap.createBitmap(
            view.width,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        view.draw(Canvas(bitmap))

        try {
            val cacheDir: File = context.cacheDir
            val imageFile = File(cacheDir, "share_image.png")
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            val imageUri: Uri =
                FileProvider.getUriForFile(context, "com.greenwaymyanmar.fileprovider", imageFile)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            context.startActivity(Intent.createChooser(shareIntent, "Share image"))

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun share3(context: Context, view: View) {
        try {
            val bitmap = view.drawToBitmap()
            val cacheDir: File = context.cacheDir
            val imageFile = File(cacheDir, "share_image.png")
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            val imageUri: Uri =
                FileProvider.getUriForFile(context, "com.greenwaymyanmar.fileprovider", imageFile)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            context.startActivity(Intent.createChooser(shareIntent, "Share image"))

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun share4(context: Context, view: View) {
        try {
            val currentLayout: View = view
            val width = currentLayout.width
            val height = currentLayout.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val backgroundDrawable = currentLayout.background
            if (backgroundDrawable != null) {
                backgroundDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            currentLayout.draw(canvas)

            val cacheDir: File = context.cacheDir
            val imageFile = File(cacheDir, "share_image.png")
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            val imageUri: Uri =
                FileProvider.getUriForFile(context, "com.greenwaymyanmar.fileprovider", imageFile)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            context.startActivity(Intent.createChooser(shareIntent, "Share image"))

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    fun View.drawToBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
        if (!ViewCompat.isLaidOut(this)) {
            throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
        }
        return Bitmap.createBitmap(width, height, config).applyCanvas {
            translate(-scrollX.toFloat(), -scrollY.toFloat())
            draw(this)
        }
    }
}