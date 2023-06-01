package com.app.shop.mylibrary.widgts.timepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import com.app.shop.mylibrary.R;

import java.util.ArrayList;
import java.util.List;


public class WarpLinearLayout extends ViewGroup {

    private Type mType;
    private List<WarpLine> mWarpLineGroup;

    public WarpLinearLayout(Context context) {
        this(context, null);
    }

    public WarpLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.WarpLinearLayoutDefault);
    }

    public WarpLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mType = new Type(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int with = 0;
        int height = 0;
        int childCount = getChildCount();

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        switch (withMode) {
            case MeasureSpec.EXACTLY:
                with = withSize;
                break;
            case MeasureSpec.AT_MOST:
                for (int i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with += mType.horizontal_Space;
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                with = with > withSize ? withSize : with;
                break;
            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with += mType.horizontal_Space;
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                break;
            default:
                with = withSize;
                break;

        }

        WarpLine warpLine = new WarpLine();

        mWarpLineGroup = new ArrayList<WarpLine>();
        for (int i = 0; i < childCount; i++) {
            if (warpLine.lineWidth + getChildAt(i).getMeasuredWidth() + mType.horizontal_Space > with) {
                if (warpLine.lineView.size() == 0) {
                    warpLine.addView(getChildAt(i));
                    mWarpLineGroup.add(warpLine);
                    warpLine = new WarpLine();
                } else {
                    mWarpLineGroup.add(warpLine);
                    warpLine = new WarpLine();
                    warpLine.addView(getChildAt(i));
                }
            } else {
                warpLine.addView(getChildAt(i));
            }
        }

        if (warpLine.lineView.size() > 0 && !mWarpLineGroup.contains(warpLine)) {
            mWarpLineGroup.add(warpLine);
        }

        height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < mWarpLineGroup.size(); i++) {
            if (i != 0) {
                height += mType.vertical_Space;
            }
            height += mWarpLineGroup.get(i).height;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = height > heightSize ? heightSize : height;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        setMeasuredDimension(with, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        t = getPaddingTop();
        for (int i = 0; i < mWarpLineGroup.size(); i++) {
            int left = getPaddingLeft();
            WarpLine warpLine = mWarpLineGroup.get(i);
            int lastWidth = getMeasuredWidth() - warpLine.lineWidth;
            for (int j = 0; j < warpLine.lineView.size(); j++) {
                View view = warpLine.lineView.get(j);
                if (isFull()) {
                    view.layout(left, t, left + view.getMeasuredWidth() + lastWidth / warpLine.lineView.size(), t + view.getMeasuredHeight());
                    left += view.getMeasuredWidth() + mType.horizontal_Space + lastWidth / warpLine.lineView.size();
                } else {
                    switch (getGrivate()) {
                        case 0:
                            view.layout(left + lastWidth, t, left + lastWidth + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                        case 2:
                            view.layout(left + lastWidth / 2, t, left + lastWidth / 2 + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                        default:
                            view.layout(left, t, left + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                            break;
                    }
                    left += view.getMeasuredWidth() + mType.horizontal_Space;
                }
            }
            t += warpLine.height + mType.vertical_Space;
        }
    }

    private final class WarpLine {
        private List<View> lineView = new ArrayList<View>();

        private int lineWidth = getPaddingLeft() + getPaddingRight();

        private int height = 0;

        private void addView(View view) {
            if (lineView.size() != 0) {
                lineWidth += mType.horizontal_Space;
            }
            height = height > view.getMeasuredHeight() ? height : view.getMeasuredHeight();
            lineWidth += view.getMeasuredWidth();
            lineView.add(view);
        }
    }


    private final static class Type {
        private int grivate;

        private float horizontal_Space;

        private float vertical_Space;

        private boolean isFull;

        Type(Context context, AttributeSet attrs) {
            if (attrs == null) {
                return;
            }
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WarpLinearLayout);
            grivate = typedArray.getInt(R.styleable.WarpLinearLayout_grivate, grivate);
            horizontal_Space = typedArray.getDimension(R.styleable.WarpLinearLayout_horizontal_Space, horizontal_Space);
            vertical_Space = typedArray.getDimension(R.styleable.WarpLinearLayout_vertical_Space, vertical_Space);
            isFull = typedArray.getBoolean(R.styleable.WarpLinearLayout_isFull, isFull);
        }
    }

    public int getGrivate() {
        return mType.grivate;
    }

    public float getHorizontal_Space() {
        return mType.horizontal_Space;
    }

    public float getVertical_Space() {
        return mType.vertical_Space;
    }

    public boolean isFull() {
        return mType.isFull;
    }

    public void setGrivate(int grivate) {
        mType.grivate = grivate;
    }

    public void setHorizontal_Space(float horizontal_Space) {
        mType.horizontal_Space = horizontal_Space;
    }

    public void setVertical_Space(float vertical_Space) {
        mType.vertical_Space = vertical_Space;
    }

    public void setIsFull(boolean isFull) {
        mType.isFull = isFull;
    }

    public final static class Gravite {
        public final static int RIGHT = 0;
        public final static int LEFT = 1;
        public final static int CENTER = 2;
    }
}