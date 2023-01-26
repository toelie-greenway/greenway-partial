package greenway_myanmar.org.util.extensions

import android.content.Context
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import greenway_myanmar.org.R

fun ImageView.load(
    context: Context, imageUrl: String?,
    @AnimRes animationId: Int = R.anim.image_fade_in,
    @DrawableRes fallbackResourceId: Int = R.drawable.image_placeholder,
    @DrawableRes errorResourceId: Int = R.drawable.image_placeholder,
    @DrawableRes placeholderResourceId: Int = R.drawable.image_placeholder,
) {
    Glide.with(context)
        .load(imageUrl)
        .animate(animationId)
        .fallback(fallbackResourceId)
        .error(errorResourceId)
        .placeholder(placeholderResourceId)
        .into(this)
}
