package greenway_myanmar.org.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import greenway_myanmar.org.R;

public class GreenWayGridMediumItem extends GreenWayGridItem {

    public GreenWayGridMediumItem(Context context) {
        this(context, null);
    }

    public GreenWayGridMediumItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GreenWayGridMediumItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.greenway_grid_medium_item;
    }
}
