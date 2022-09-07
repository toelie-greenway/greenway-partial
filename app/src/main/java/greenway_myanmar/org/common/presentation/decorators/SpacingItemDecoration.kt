package greenway_myanmar.org.common.presentation.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.dpToPx
import greenway_myanmar.org.util.UIUtils

/** ItemDecoration that adds space around items. */
class SpacingItemDecoration : RecyclerView.ItemDecoration {
  private var left: Int
  private var top: Int
  private var right: Int
  private var bottom: Int

  constructor(context: Context, marginSizeInDp: Int) {
    val margin = UIUtils.dpToPx(context, marginSizeInDp)
    left = margin
    top = margin
    right = margin
    bottom = margin
  }

  constructor(context: Context, left: Int, top: Int, right: Int, bottom: Int) {
    this.left = UIUtils.dpToPx(context, left)
    this.top = UIUtils.dpToPx(context, top)
    this.right = UIUtils.dpToPx(context, right)
    this.bottom = UIUtils.dpToPx(context, bottom)
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    outRect.set(left, top, right, bottom)
  }
}
