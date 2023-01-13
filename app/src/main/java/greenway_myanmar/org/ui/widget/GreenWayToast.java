package greenway_myanmar.org.ui.widget;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class GreenWayToast {

    public static void showToast(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text.toString(), duration).show();
    }

    public static void showToast(Context context, @StringRes int resId, int duration) {
        showToast(context, context.getResources().getString(resId), duration);
    }
}
