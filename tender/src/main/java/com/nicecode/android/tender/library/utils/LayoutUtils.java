package com.nicecode.android.tender.library.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nicecode.android.tender.BuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public final class LayoutUtils {

    @SuppressWarnings("unused")
    public static void loadImage(ImageView target, int targetResourceId, int substResourceId) {
        DisplayImageOptions mTempOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(substResourceId)
                .showImageForEmptyUri(substResourceId)
                .showImageOnFail(substResourceId)
//                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
        String imageUri = "drawable://" + targetResourceId;
        ImageLoader.getInstance().displayImage(imageUri, target, mTempOptions);
    }

    @SuppressWarnings("unused")
    public static View cleanLayout(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i= 0; i < viewGroup.getChildCount(); i++)  {
                    cleanLayout(viewGroup.getChildAt(i));
                }
                if( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    viewGroup.setBackgroundDrawable(null);
                } else {
                    viewGroup.setBackground(null);
                }
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    listView.setAdapter(null);
                } else {
                    viewGroup.removeAllViews();
                }
            } else if (view instanceof ImageView) {
                cleanImage((ImageView) view);
            } else if (view instanceof TextView) {
                cleanText((TextView) view);
            } else if (!(view instanceof ProgressBar)) {
                if (!(view instanceof View)) {
                    if (BuildConfig.DEBUG) Log.d("Error:", "Clean error");
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static ImageView cleanImage(ImageView image) {
        if (image != null) {
            Drawable drawable = image.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if ((bitmap != null) && (!bitmap.isRecycled())) {
                    bitmap.recycle();
                }
            }
            image.setImageDrawable(null);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static Drawable cleanDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if ((bitmap != null) && (!bitmap.isRecycled())) {
                    bitmap.recycle();
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static TextView cleanText(TextView textView) {
        textView.setText(null);
        return null;
    }

    public static void cleanAndlLoadImage(ImageView image, int resourceId) {
        if (image != null) {
            Drawable drawable = image.getDrawable();
            loadImage(image, resourceId, resourceId);
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if ((bitmap != null) && (!bitmap.isRecycled())) {
                    bitmap.recycle();
                }
            }
        }
    }
}
