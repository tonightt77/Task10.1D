package com.app.shop.mylibrary.utils;

import android.app.Application;
import android.net.Uri;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

public class FrescoUtil {


    /**
     * loading pic
     *
     * @param url
     * @param simpleDraweeView
     */
    public static void setBasePic(String url, SimpleDraweeView simpleDraweeView) {
        Uri parse = Uri.parse(url);
        simpleDraweeView.setImageURI(parse);
    }

    /**
     * Progressive image loading
     *
     * @param url
     * @param simpleDraweeView
     */
    public static void setJianPic(String url, SimpleDraweeView simpleDraweeView) {
        if (!StringUtil.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            simpleDraweeView.setController(controller);
        }
    }

    /**
     *
     * @param url
     * @param simpleDraweeView
     * @param radius
     * @param color
     * @param width
     */
    public static void setCirclePic(String url, SimpleDraweeView simpleDraweeView, float radius, int color, float width) {
        Uri uri = Uri.parse(url);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(0f);
        if (width > 0f) {
            roundingParams.setBorder(color, width);
        }
        roundingParams.setCornersRadius(radius);//set radius
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        simpleDraweeView.setImageURI(uri);

    }

    /**
     * @param url
     * @param simpleDraweeView
     * @param topLeft
     * @param topRight
     * @param bottomRight
     * @param bottomLeft
     * @param color
     * @param width
     */
    public static void setCotrolCirclePic(String url, SimpleDraweeView simpleDraweeView, float topLeft, float topRight, float bottomRight, float bottomLeft, int color, float width) {
        Uri uri = Uri.parse(url);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(0f);
        if (width > 0f) {
            roundingParams.setBorder(color, width);
        }
        roundingParams.setCornersRadii(topLeft, topRight, bottomLeft, bottomRight);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        simpleDraweeView.setImageURI(uri);
    }

    /**
     *
     * @param url
     * @param simpleDraweeView
     * @param color
     * @param width
     */
    public static void setYuanPic(String url, SimpleDraweeView simpleDraweeView, int color, float width) {
        Uri uri = Uri.parse(url);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(0f);
        if (width > 0f) {
            roundingParams.setBorder(color, width);
        }
        roundingParams.setRoundAsCircle(true);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        simpleDraweeView.setImageURI(uri);
    }

    /**
     * gif
     *
     * @param url
     * @param simpleDraweeView
     */
    public static void setDongPic(String url, SimpleDraweeView simpleDraweeView) {
        Uri uri = Uri.parse(url);
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)//Set to true to loop the Gif animation
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller1);
    }

    /**
     * Gaussian blur
     *
     * @param url
     * @param simpleDraweeView
     * @param iterations
     * @param blurRadius
     */
    public static void setUrlBlur(String url, SimpleDraweeView simpleDraweeView, int iterations, int blurRadius) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                .build();
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * custom cache
     */
    public static void init(Application application) {

        Fresco.initialize(application, ImagePipelineConfig.newBuilder(application)
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(application)
                                .setBaseDirectoryName("image")
                                .setBaseDirectoryPath(new File(AppDir.APP_CACHE + File.separator))
                                .build()
                )
                .build());
    }
}



