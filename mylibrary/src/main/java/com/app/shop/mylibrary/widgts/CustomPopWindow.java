package com.app.shop.mylibrary.widgts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;


public class CustomPopWindow implements PopupWindow.OnDismissListener {
    private static final String TAG = "CustomPopWindow";
    private static final float DEFAULT_ALPHA = 0.7f;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private Drawable mBackgroundDrawable = new ColorDrawable(Color.TRANSPARENT);
    private int mResLayoutId = -1;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClippEnable = true;//default is true
    private boolean mIgnoreCheekPress = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;//default is ture
    private View.OnTouchListener mOnTouchListener;

    private Window mWindow;//current Activity

    private boolean mIsBackgroundDark = false;

    private float mBackgroundDrakValue = 0;

    private boolean enableOutsideTouchDisMiss = true;

    private CustomPopWindow(Context context) {
        mContext = context;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    /**
     * @param anchor
     * @param xOff
     * @param yOff
     * @return
     */
    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    public CustomPopWindow showAsDropDown(View anchor) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }


    public CustomPopWindow showAtLocation(View parent, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }


    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(mClippEnable);
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        if (mInputMode != -1) {
            popupWindow.setInputMethodMode(mInputMode);
        }
        if (mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener);
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);


    }

    private PopupWindow build() {

        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null);
        }


        Activity activity = (Activity) mContentView.getContext();
        if (activity != null && mIsBackgroundDark) {
            //If the set value is in the range of 0 - 1, use the set value, otherwise use the default value
            final float alpha = (mBackgroundDrakValue > 0 && mBackgroundDrakValue < 1) ? mBackgroundDrakValue : DEFAULT_ALPHA;
            mWindow = activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = alpha;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }


        if (mWidth != 0 && mHeight != 0) {
            mPopupWindow = new PopupWindow(mContentView, mWidth, mHeight);
        } else {
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mAnimationStyle != -1) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }

        apply(mPopupWindow);

        if (mWidth == 0 || mHeight == 0) {
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //If the width and height are not set, calculate the width and height and assign them
            mWidth = mPopupWindow.getContentView().getWidth();
            mHeight = mPopupWindow.getContentView().getHeight();
        }

        mPopupWindow.setOnDismissListener(this);

        // Determine whether to click on a place other than PopupWindow to close popWindow
        if (!enableOutsideTouchDisMiss) {

            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mPopupWindow.dismiss();

                        return true;
                    }
                    return false;
                }
            });
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int x = (int) event.getX();
                    final int y = (int) event.getY();
                    mWidth = mPopupWindow.getContentView().getWidth();
                    mHeight = mPopupWindow.getContentView().getHeight();
                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mPopupWindow.setFocusable(mIsFocusable);
            mPopupWindow.setBackgroundDrawable(mBackgroundDrawable);
            mPopupWindow.setOutsideTouchable(mIsOutside);
        }
        // update
        mPopupWindow.update();

        return mPopupWindow;
    }

    @Override
    public void onDismiss() {
        dissmiss();
    }

    /**
     * close popWindow
     */
    public void dissmiss() {

        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }

        //If the background is darkened, it needs to be restored when dissmiss
        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 1.0f;
            mWindow.setAttributes(params);
        }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public static class PopupWindowBuilder {
        private CustomPopWindow mCustomPopWindow;

        public PopupWindowBuilder(Context context) {
            mCustomPopWindow = new CustomPopWindow(context);
        }

        public PopupWindowBuilder size(int width, int height) {
            mCustomPopWindow.mWidth = width;
            mCustomPopWindow.mHeight = height;
            return this;
        }


        public PopupWindowBuilder setFocusable(boolean focusable) {
            mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }


        public PopupWindowBuilder setView(int resLayoutId) {
            mCustomPopWindow.mResLayoutId = resLayoutId;
            mCustomPopWindow.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            mCustomPopWindow.mContentView = view;
            mCustomPopWindow.mResLayoutId = -1;
            return this;
        }



        public PopupWindowBuilder enableBackgroundDark(boolean isDark) {
            mCustomPopWindow.mIsBackgroundDark = isDark;
            return this;
        }


        public PopupWindowBuilder setBgDarkAlpha(float darkValue) {
            mCustomPopWindow.mBackgroundDrakValue = darkValue;
            return this;
        }




        public CustomPopWindow create() {

            mCustomPopWindow.build();
            return mCustomPopWindow;
        }

    }

}
