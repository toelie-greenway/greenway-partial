package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.SeasonSummaryItemUiState
import greenway_myanmar.org.util.UIUtils

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SeasonSummaryItemViewGroup
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

  private val rootView: LinearLayout by lazy {
    this
  }

  init {
    setupRootView(context)
  }

  private fun setupRootView(context: Context) {
    rootView.setBackgroundColor(Color.WHITE)
    rootView.orientation = VERTICAL
    rootView.setPadding(UIUtils.dpToPx(context, 16), 0, 0, 0)

    val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    layoutParams.bottomMargin = UIUtils.dpToPx(context, 8)
  }

  @JvmOverloads
  @ModelProp
  fun items(list: List<SeasonSummaryItemUiState> = emptyList()) {
    generateViews(list)
  }

  private fun generateViews(list: List<SeasonSummaryItemUiState>) {
    rootView.removeAllViews()

    val total = list.size

    list.forEachIndexed { index, item ->
      val itemView = SeasonSummaryItemView(context).apply {
        bind(item)
      }
      rootView.addView(itemView)

      if (index < total - 1) {
        rootView.addView(createDivider())
      }
    }
  }

  private fun createDivider(): View {
    val divider = View(context)
    val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dpToPx(context, 1))
    divider.layoutParams = lp
    divider.setBackgroundColor(ContextCompat.getColor(context, R.color.app_divider))
    return divider
  }
}
