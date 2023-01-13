package greenway_myanmar.org.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import greenway_myanmar.org.R;

public abstract class GreenWayGridItem extends LinearLayout implements Checkable {

    private final Context context;
    private boolean mChecked;
    private boolean mBroadcasting;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private String label;
    private int iconResId;
    private String imageUrl;
    private GreenWayTextView labelTextView;
    private ImageView iconImageView;
    private int placeholderResId;

    private ImageView checkImageView;
    private FrameLayout checkFrame;

    public GreenWayGridItem(Context context) {
        this(context, null);
    }

    public GreenWayGridItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GreenWayGridItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GreenWayCommon);

        label = a.getString(R.styleable.GreenWayCommon_label);
        iconResId = a.getResourceId(R.styleable.GreenWayCommon_icon, 0);
        imageUrl = a.getString(R.styleable.GreenWayCommon_imageUrl);
        placeholderResId = a.getResourceId(R.styleable.GreenWayCommon_placeholder, 0);

        a.recycle();
        init();
    }

    private void init() {
        final View root = LayoutInflater.from(context)
                .inflate(getLayoutId(), this, true);

        labelTextView = root.findViewById(R.id.text);
        if (!TextUtils.isEmpty(label)) {
            labelTextView.setText(label);
        }

        iconImageView = root.findViewById(R.id.icon);
        if (iconResId > 0) {
            iconImageView.setImageResource(iconResId);
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            setImageUrl(imageUrl);
        }


        checkImageView = root.findViewById(R.id.check_icon);
        checkFrame = root.findViewById(R.id.check_frame);

        refreshColor();
        setClickable(true);
    }

    protected abstract int getLayoutId();

    public void setLabel(String text) {
        this.label = text;
        if (!TextUtils.isEmpty(text)) {
            labelTextView.setText(text);
        }
    }

    public void setIcon(int iconResId) {
        this.iconResId = iconResId;
        if (iconResId > 0) {
            iconImageView.setImageResource(iconResId);
        }
    }

    public void setPlaceholderResId(int placeholderResId) {
        this.placeholderResId = placeholderResId;
    }

    public void setImageUrl(String imageUrl, int placeholderId) {
        this.placeholderResId = placeholderId;
        setImageUrl(imageUrl);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        RequestManager glide = Glide.with(getContext());
        if (!TextUtils.isEmpty(imageUrl) && placeholderResId != 0) {
            glide.load(imageUrl).animate(R.anim.image_fade_in).placeholder(placeholderResId).into(iconImageView);
        } else if (!TextUtils.isEmpty(imageUrl)) {
            glide.load(imageUrl).animate(R.anim.image_fade_in).into(iconImageView);
        } else {
            iconImageView.setImageResource(placeholderResId);
        }
    }

    @Override
    public void toggle() {
        // we override to prevent toggle when the show day is already
        // checked (as opposed to check boxes widgets)
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @ViewDebug.ExportedProperty
    @Override
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {

            mChecked = checked;
            refreshColor();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }

            mBroadcasting = false;
        }
    }

    private void refreshColor() {
        if (mChecked) {
            checkImageView.setVisibility(VISIBLE);
            checkFrame.setVisibility(VISIBLE);
            labelTextView.setTextColor(ContextCompat.getColor(context, R.color.app_primary_text));
            /*setBackgroundResource(R.drawable.voting_button_selected);
            mLabelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.theme_primary));
            mCountTextView.setBackgroundResource(R.drawable.voting_button_count_selected);
            DrawableCompat.setTint(mIconImageView.getDrawable(), ContextCompat.getColor(mContext, R.color.theme_primary));*/
        } else {
            checkImageView.setVisibility(GONE);
            checkFrame.setVisibility(GONE);
            labelTextView.setTextColor(ContextCompat.getColor(context, R.color.app_secondary_text));
            /*setBackgroundResource(R.drawable.voting_button_normal);
            mLabelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_inactive_icon));
            mCountTextView.setBackgroundResource(R.drawable.voting_button_count_normal);
            DrawableCompat.setTint(mIconImageView.getDrawable(), ContextCompat.getColor(mContext, R.color.app_inactive_icon));*/
        }
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return GreenWayGridItem.class.getName();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param compoundView The compound button view whose state has changed.
         * @param isChecked    The new checked state of buttonView.
         */
        void onCheckedChanged(GreenWayGridItem compoundView, boolean isChecked);
    }

    static class SavedState extends BaseSavedState {
        @SuppressWarnings("hiding")
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        boolean checked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "CompoundButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }
    }
}
