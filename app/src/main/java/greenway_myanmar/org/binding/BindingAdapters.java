/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package greenway_myanmar.org.binding;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import javax.annotation.Nullable;

import greenway_myanmar.org.R;
import greenway_myanmar.org.util.UIUtils;

/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showGone(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("goneVisible")
    public static void goneShow(View view, boolean gone) {
        view.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("visibleInvisible")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("srcResId")
    public static void setDrawableResId(ImageView imageView, int imageResId) {
        imageView.setImageResource(imageResId);
    }

    @BindingAdapter("srcCompat")
    public static void setDrawable(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    @BindingAdapter(value = {"endIconMode", "endIconOnClickListener"}, requireAll = false)
    public static void bindEndIcon(TextInputLayout view, @TextInputLayout.EndIconMode int endIconMode, @Nullable View.OnClickListener endIconOnClickListener) {
        view.setEndIconMode(endIconMode);
        view.setEndIconOnClickListener(endIconOnClickListener);
    }

    @BindingAdapter("imagePath")
    public static void setImagePath(ImageView imageView, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Glide.with(imageView.getContext())
                    .load(imageFile)
                    .into(imageView);
        }
    }

    @BindingAdapter("checked")
    public static void setImageTintList(ImageView imageView, boolean state) {
        ImageViewCompat.setImageTintList(imageView, AppCompatResources.getColorStateList(imageView.getContext(), state ? R.color.theme_primary : R.color.app_secondary_text));
    }

    @BindingAdapter("cardBackgroundColor")
    public static void setCardBackgroundColor(CardView cardView, String color) {
        if (!TextUtils.isEmpty(color)) {
            cardView.setCardBackgroundColor(Color.parseColor(color));
        }
    }

    @BindingAdapter("tint")
    public static void setImageTint(ImageView imageView, String color) {
        if (!TextUtils.isEmpty(color)) {
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(Color.parseColor(color)));
        }
    }

    @BindingAdapter("drawableTint")
    public static void setImageTint(ImageView imageView, int color) {
        if (color != -1) {
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
        }
    }

    @BindingAdapter("textColor")
    public static void setTextColor(TextView textView, String color) {
        if (!TextUtils.isEmpty(color)) {
            textView.setTextColor(ColorStateList.valueOf(Color.parseColor(color)));
        }
    }

    @BindingAdapter("textColor")
    public static void setTextColor(Button button, String color) {
        if (!TextUtils.isEmpty(color)) {
            button.setTextColor(ColorStateList.valueOf(Color.parseColor(color)));
        }
    }

    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(MaterialButton button, String color) {
        if (!TextUtils.isEmpty(color)) {
            ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(Color.parseColor(color)));
        }
    }

    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(MaterialButton button, int color) {
        if (color != -1) {
            ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(color));
        }
    }

    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(AppCompatTextView textView, int color) {
        if (color != -1) {
            ViewCompat.setBackgroundTintList(textView, ColorStateList.valueOf(color));
        }
    }

    //TODO: Migrate PagerIndicator
//    @BindingAdapter("selectedColor")
//    public static void setPageIndicatorSelectedColor(LinePageIndicator indicator, String color) {
//        if (!TextUtils.isEmpty(color)) {
//            indicator.setSelectedColor(Color.parseColor(color));
//        }
//    }

    @BindingAdapter("background")
    public static void setBackground(View view, String color) {
        if (!TextUtils.isEmpty(color)) {
            ViewCompat.setBackground(view, new ColorDrawable(Color.parseColor(color)));
        }
    }

    @BindingAdapter("drawableEndCompat")
    public static void setDrawableCompatEnd(TextInputEditText textInputEditText, Drawable drawable) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(textInputEditText, null, null, drawable, null);
    }

    @BindingAdapter("drawableStartCompat")
    public static void setDrawableCompatStart(TextView textView, Drawable drawable) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable, null, null, null);
    }

    @BindingAdapter("safeText")
    public static void setSafeTextResource(TextView textView, int textResId) {
        if (textResId <= 0) return;
        textView.setText(textResId);
    }

    @BindingAdapter("layout_marginTop")
    public static void setMarginTop(View view, int marginTop) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.setMargins(lp.leftMargin, UIUtils.dpToPx(view.getContext(), marginTop), lp.rightMargin, lp.bottomMargin);
    }

    @BindingAdapter("layout_marginBottom")
    public static void setMarginBottom(View view, int marginBottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, UIUtils.dpToPx(view.getContext(), marginBottom));
    }

    @BindingAdapter(value = {"imageUrl", "placeholder", "showLoading", "imageFilePath"}, requireAll = false)
    public static void bindImageWithPlaceholder(ImageView imageView, String url, Drawable placeholder, boolean showLoading, String imageFilePath) {

        RequestManager glide = Glide.with(imageView.getContext());
        if (!TextUtils.isEmpty(url) && showLoading) {
            // prepare progress
            CircularProgressDrawable progress = new CircularProgressDrawable(imageView.getContext());
            progress.setStyle(CircularProgressDrawable.LARGE);
            progress.setColorSchemeColors(R.color.theme_accent, R.color.theme_primary_dark, R.color.theme_accent_dark);
            progress.start();
            glide.load(url).placeholder(progress).animate(R.anim.image_fade_in).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else if (!TextUtils.isEmpty(url) && placeholder != null) {
            glide.load(url).placeholder(placeholder).animate(R.anim.image_fade_in).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else if (!TextUtils.isEmpty(url)) {
            glide.load(url).animate(R.anim.image_fade_in).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else if (!TextUtils.isEmpty(imageFilePath)) {
            glide.load(new File(imageFilePath)).animate(R.anim.image_fade_in).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else if (placeholder != null) {
            imageView.setImageDrawable(placeholder);
        }
    }
//    @BindingAdapter(value = {"lottie_rawResId", "lottie_autoPlay"})
//    public static void setLottieRawResId(LottieAnimationView lottieAnimationView, @RawRes final int rawRes, boolean autoPlay) {
//        if (rawRes != 0) {
//            lottieAnimationView.setAnimation(rawRes);
//            if (autoPlay) {
//                lottieAnimationView.playAnimation();
//            }
//        }
//    }
}
