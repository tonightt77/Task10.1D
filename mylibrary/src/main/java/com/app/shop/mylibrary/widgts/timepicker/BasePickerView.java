package com.app.shop.mylibrary.widgts.timepicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.app.shop.mylibrary.R;

public class BasePickerView {
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );

    private Context context;
    protected ViewGroup contentContainer;
    public ViewGroup decorView;
    private ViewGroup rootView;
    private ViewGroup dialogView;

    private int MATCH_PARENT_WRAP_CONTENT = 1;

    protected int pickerview_timebtn_nor = 0xFF057dff;
    protected int pickerview_timebtn_pre = 0xFFc2daf5;
    protected int pickerview_bg_topbar = 0xFFf5f5f5;
    protected int pickerview_topbar_title = 0xFF000000;
    protected int bgColor_default = 0xFFFFFFFF;

    private OnDismissListener onDismissListener;
    private boolean dismissing;

    private Animation outAnim;
    private Animation inAnim;
    private boolean isShowing;
    private int gravity = Gravity.BOTTOM;


    private Dialog mDialog;
    private boolean cancelable;

    protected View clickView;

    public BasePickerView(Context context) {
        this.context = context;

    }

    protected void initViews(int backgroudId) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (isDialog()) {
            // if it is dialog
            dialogView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, null, false);
            //Set the background of the interface to be transparent
            dialogView.setBackgroundColor(Color.TRANSPARENT);
            //This is the parent layout that actually loads the time picker
            contentContainer = (ViewGroup) dialogView.findViewById(R.id.content_container);
            //Setting dialog box left and right spacing screen 80
            this.params.leftMargin = 80;
            this.params.rightMargin = 80;
            contentContainer.setLayoutParams(this.params);
            //create dialog
            createDialog();
            //Set a click event for the background, so that the interface will be closed when the click is outside the content
            dialogView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            if (decorView == null) {
                decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
            }
            rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, decorView, false);
            rootView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ));
            if (backgroudId != 0) {
                rootView.setBackgroundColor(backgroudId);
            }
            contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
            contentContainer.setLayoutParams(params);
        }

        setKeyBackCancelable(true);

    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    protected void initEvents() {
    }

    private void onAttached(View view) {
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }

    public void show() {
        if (isDialog()) {
            showDialog();
        } else {
            if (isShowing()) {
                return;
            }
            isShowing = true;
            onAttached(rootView);
            rootView.requestFocus();
        }
    }


    public void show(View v) {
        this.clickView = v;
        show();
    }


    public boolean isShowing() {
        if (isDialog()) {
            return false;
        } else {
            return rootView.getParent() != null || isShowing;
        }

    }

    public void dismiss() {
        if (isDialog()) {
            dismissDialog();
        } else {
            if (dismissing) {
                return;
            }

            dismissing = true;

            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    decorView.post(new Runnable() {
                        @Override
                        public void run() {
                            dismissImmediately();
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            contentContainer.startAnimation(outAnim);
        }

    }

    public void dismissImmediately() {
        decorView.removeView(rootView);
        isShowing = false;
        dismissing = false;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(BasePickerView.this);
        }

    }

    public Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public BasePickerView setKeyBackCancelable(boolean isCancelable) {

        ViewGroup View;
        if (isDialog()) {
            View = dialogView;
        } else {
            View = rootView;
        }

        View.setFocusable(isCancelable);
        View.setFocusableInTouchMode(isCancelable);
        if (isCancelable) {
            View.setOnKeyListener(onKeyBackListener);
        } else {
            View.setOnKeyListener(null);
        }
        return this;
    }

    private View.OnKeyListener onKeyBackListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN
                    && isShowing()) {
                dismiss();
                return true;
            }
            return false;
        }
    };

    protected BasePickerView setOutSideCancelable(boolean isCancelable) {
        if (rootView != null) {
            View view = rootView.findViewById(R.id.outmost_container);

            if (isCancelable) {
                view.setOnTouchListener(onCancelableTouchListener);
            } else {
                view.setOnTouchListener(null);
            }
        }

        return this;
    }

    public void setDialogOutSideCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }


    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id) {
        return contentContainer.findViewById(id);
    }

    public void createDialog() {
        if (dialogView != null) {
            mDialog = new Dialog(context, R.style.custom_dialog2);
            mDialog.setCancelable(cancelable);
            mDialog.setContentView(dialogView);

            mDialog.getWindow().setWindowAnimations(R.style.pickerview_dialogAnim);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null) {
                        onDismissListener.onDismiss(BasePickerView.this);
                    }
                }
            });
        }

    }

    public void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public boolean isDialog() {
        return false;
    }
}
