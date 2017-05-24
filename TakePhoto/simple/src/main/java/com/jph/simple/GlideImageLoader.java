package com.jph.simple;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

/**
 * Created by 被咯苏州 on 2017/5/24.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(path)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
