package greenway_myanmar.org.features.thread.presentation.models
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.widget.LinearLayout
//import com.airbnb.epoxy.CallbackProp
//import com.airbnb.epoxy.ModelView
//import greenway_myanmar.org.R
//import greenway_myanmar.org.databinding.ThreadDetailMoreCommentViewBinding
//
//@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
//class ThreadMoreCommentView
//@JvmOverloads
//constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
//  LinearLayout(context, attrs, defStyleAttr) {
//
//  private val binding =
//    ThreadDetailMoreCommentViewBinding.inflate(LayoutInflater.from(context), this)
//
//  var onMoreClickCallback: OnClickListener? = null
//    @CallbackProp set
//
//  init {
//    orientation = VERTICAL
//    setPadding(
//      resources.getDimensionPixelSize(R.dimen.spacing_8),
//      0,
//      resources.getDimensionPixelSize(R.dimen.spacing_8),
//      0
//    )
//
//    binding.moreButton.setOnClickListener {
//      onMoreClickCallback?.onClick(it)
//    }
//  }
//}
