package com.app.shop.mylibrary.utils;

import android.content.Context;
import android.widget.ImageView;

import com.app.shop.mylibrary.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class GlideUtils {

    static GlideUtils instance;

    public GlideUtils() {
    }

    public static GlideUtils getInstance() {
        if (null == instance) {
            synchronized (GlideUtils.class) {
                if (null == instance) {
                    instance = new GlideUtils();
                }
            }
        }
        return instance;
    }

    /**
     * Load a rectangular image file to the imageView
     * @param context
     * @param imageView
     * @param url
     */
    public void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public void loadImage(Context context, String url, ImageView imageView, int imgv_default) {
        if (imgv_default == 0) {
            imgv_default = R.mipmap.ic_launcher;
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(imgv_default)
                .error(imgv_default);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public void loadImageForFile(Context context, ImageView imageView, File file) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context)
                .load(file)
                .thumbnail(0.1f)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load circle image
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * load Circle With Frame Image
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadCircleWithFrameImage(Context context, String url, ImageView imageView, int borderWidth, int borderColor) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new GlideCircleWithFrameTransform(borderWidth, borderColor))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * load Round Image
     * @param context
     * @param url
     * @param imageView
     */
    public void loadRoundImage(Context context, String url, ImageView imageView, int corner) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(corner))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * load Top Round Image
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadTopRoundImage(Context context, String url, ImageView imageView, int corner) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new GlideRoundTransform(corner))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        Glide.with(context).load(url).apply(options).into(imageView);
    }


    /**
     * load Image GIF
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadImageGIF(Context mContext, String url, ImageView imageView) {
        Glide.with(mContext)
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }
}
