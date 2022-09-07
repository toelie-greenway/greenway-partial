package greenway_myanmar.org.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by toe-lie on 6/15/2017.
 */

public class UIUtils {

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float spToPx(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static AppCompatActivity getActivityFrom(Context context) {
        // Gross way of unwrapping the Activity so we can get the FragmentManager
        while (context instanceof ContextWrapper) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
