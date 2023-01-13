package greenway_myanmar.org.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.checkbox.MaterialCheckBox;

public class GreenWayCheckBox extends MaterialCheckBox {

    private String mValue;

    public GreenWayCheckBox(Context context) {
        super(context);
    }

    public GreenWayCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GreenWayCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
