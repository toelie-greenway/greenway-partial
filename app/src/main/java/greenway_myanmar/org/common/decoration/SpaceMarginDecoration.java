package greenway_myanmar.org.common.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import greenway_myanmar.org.util.UIUtils;

/**
 * ItemDecoration that adds space around items.
 */
public class SpaceMarginDecoration extends RecyclerView.ItemDecoration {
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public SpaceMarginDecoration(Context context, int marginSizeInDp) {
        int margin = UIUtils.dpToPx(context, marginSizeInDp);
        left = margin;
        top = margin;
        right = margin;
        bottom = margin;
    }

    public SpaceMarginDecoration(Context context, int left, int top, int right, int bottom) {
        this.left = UIUtils.dpToPx(context, left);
        this.top = UIUtils.dpToPx(context, top);
        this.right = UIUtils.dpToPx(context, right);
        this.bottom = UIUtils.dpToPx(context, bottom);
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(left, top, right, bottom);
    }
}