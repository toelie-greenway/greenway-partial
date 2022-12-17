package greenway_myanmar.org.ui.widget.motion

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.util.ObjectsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout

class CollapsibleToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), AppBarLayout.OnOffsetChangedListener {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //(parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//        progress = -verticalOffset / (appBarLayout?.totalScrollRange?.toFloat() ?: 1f)
//        Timber.d("verticalOffset: $verticalOffset")
//        Timber.d("totalScrollRange: ${appBarLayout?.totalScrollRange?.toFloat()}")
//        Timber.d("progress: $progress")
    }


}