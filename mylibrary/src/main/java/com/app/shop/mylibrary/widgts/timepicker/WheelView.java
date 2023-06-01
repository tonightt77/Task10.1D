package com.app.shop.mylibrary.widgts.timepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;


import com.app.shop.mylibrary.R;
import com.app.shop.mylibrary.interfaces.IPickerViewData;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WheelView extends View {

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    public enum DividerType {
        FILL, WRAP
    }

    private DividerType dividerType;

    ACTION action_now;
    Context context;

    Handler handler;
    private GestureDetector gestureDetector;
    OnItemSelectedListener onItemSelectedListener;

    private boolean isOptions = false;
    private boolean isCenterLabel = true;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    Paint paintOuterText;
    Paint paintCenterText;
    Paint paintIndicator;

    WheelAdapter adapter;

    private String label;
    int textSize;
    int maxTextWidth;
    int maxTextHeight;
    float itemHeight;

    Typeface typeface = Typeface.MONOSPACE;//

    int textColorOut = 0xFFa8a8a8;
    int textColorCenter = 0xFF2a2a2a;
    int dividerColor = 0xFFd5d5d5;

    // 条目间距倍数
    float lineSpacingMultiplier = 1.6F;
    boolean isLoop;

    float firstLineY;
    float secondLineY;
    float centerY;

    float totalScrollY;

    int initPosition;

    private int selectedItem;
    int preCurrentIndex;

    int change;

    int itemsVisible = 9;

    int measuredHeight;
    int measuredWidth;

    int halfCircumference;

    int radius;

    private int mOffset = 0;
    private float previousY = 0;
    long startTime = 0;

    private static final int VELOCITYFLING = 5;
    int widthMeasureSpec;

    private int mGravity = Gravity.CENTER;
    private int drawCenterContentStart = 0;
    private int drawOutContentStart = 0;
    private static final float SCALECONTENT = 0.7F;
    private float CENTERCONTENTOFFSET;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
       /* textColorOut = getResources().getColor(R.color.pickerview_wheelview_textcolor_out);
        textColorCenter =getResources().getColor(R.color.pickerview_wheelview_textcolor_center);
        dividerColor = getResources().getColor(R.color.pickerview_wheelview_textcolor_out);*/

        textSize = getResources().getDimensionPixelSize(R.dimen.textSize_20sp);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density; // screen density（0.75/1.0/1.5/2.0/3.0）

        if (density < 1) {
            CENTERCONTENTOFFSET = 2.4F;
        } else if (1 <= density && density < 2) {
            CENTERCONTENTOFFSET = 3.6F;
        } else if (1 <= density && density < 2) {
            CENTERCONTENTOFFSET = 4.5F;
        } else if (2 <= density && density < 3) {
            CENTERCONTENTOFFSET = 6.0F;
        } else if (density >= 3) {
            CENTERCONTENTOFFSET = density * 2.5F;
        }


        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pickerview, 0, 0);
            mGravity = a.getInt(R.styleable.pickerview_pickerview_gravity, Gravity.CENTER);
            textColorOut = a.getColor(R.styleable.pickerview_pickerview_textColorOut, textColorOut);
            textColorCenter = a.getColor(R.styleable.pickerview_pickerview_textColorCenter, textColorCenter);
            dividerColor = a.getColor(R.styleable.pickerview_pickerview_dividerColor, dividerColor);
            textSize = a.getDimensionPixelOffset(R.styleable.pickerview_pickerview_textSize, textSize);
            lineSpacingMultiplier = a.getFloat(R.styleable.pickerview_pickerview_lineSpacingMultiplier, lineSpacingMultiplier);
            a.recycle();
        }

        judgeLineSpae();

        initLoopView(context);
    }

    private void judgeLineSpae() {
        if (lineSpacingMultiplier < 1.2f) {
            lineSpacingMultiplier = 1.2f;
        } else if (lineSpacingMultiplier > 2.0f) {
            lineSpacingMultiplier = 2.0f;
        }
    }

    private void initLoopView(Context context) {
        this.context = context;
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        gestureDetector.setIsLongpressEnabled(false);

        isLoop = true;

        totalScrollY = 0;
        initPosition = -1;

        initPaints();

    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(textColorOut);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(typeface);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(textColorCenter);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(1.1F);
        paintCenterText.setTypeface(typeface);
        paintCenterText.setTextSize(textSize);


        paintIndicator = new Paint();
        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void remeasure() {
        if (adapter == null) {
            return;
        }

        measureTextWidthHeight();


        halfCircumference = (int) (itemHeight * (itemsVisible - 1));

        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);

        radius = (int) (halfCircumference / Math.PI);

        measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

        firstLineY = (measuredHeight - itemHeight) / 2.0F;
        secondLineY = (measuredHeight + itemHeight) / 2.0F;
        centerY = secondLineY - (itemHeight - maxTextHeight) / 2.0f - CENTERCONTENTOFFSET;

        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (adapter.getItemsCount() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }
        preCurrentIndex = initPosition;
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < adapter.getItemsCount(); i++) {
            String s1 = getContentText(adapter.getItem(i));
            paintCenterText.getTextBounds(s1, 0, s1.length(), rect);

            int textWidth = rect.width();

            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect);

            maxTextHeight = rect.height() + 2;

        }
        itemHeight = lineSpacingMultiplier * maxTextHeight;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        action_now = action;
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }

        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, VELOCITYFLING, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }


    public final void setCyclic(boolean cyclic) {
        isLoop = cyclic;
    }

    public final void setTypeface(Typeface font) {
        typeface = font;
        paintOuterText.setTypeface(typeface);
        paintCenterText.setTypeface(typeface);
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setCurrentItem(int currentItem) {
        this.initPosition = currentItem;
        totalScrollY = 0;
        invalidate();
    }

    public final void setOnItemSelectedListener(OnItemSelectedListener OnItemSelectedListener) {
        this.onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        remeasure();
        invalidate();
    }

    public final WheelAdapter getAdapter() {
        return adapter;
    }

    public final int getCurrentItem() {
        return selectedItem;
    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (adapter == null) {
            return;
        }
        Object visibles[] = new Object[itemsVisible];

        change = (int) (totalScrollY / itemHeight);

        try {
            preCurrentIndex = initPosition + change % adapter.getItemsCount();

        } catch (ArithmeticException e) {
            Log.e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配");
        }
        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > adapter.getItemsCount() - 1) {
                preCurrentIndex = adapter.getItemsCount() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = adapter.getItemsCount() + preCurrentIndex;
            }
            if (preCurrentIndex > adapter.getItemsCount() - 1) {
                preCurrentIndex = preCurrentIndex - adapter.getItemsCount();
            }
        }
        float itemHeightOffset = (totalScrollY % itemHeight);

        int counter = 0;
        while (counter < itemsVisible) {
            int index = preCurrentIndex - (itemsVisible / 2 - counter);
            if (isLoop) {
                index = getLoopMappingIndex(index);
                visibles[counter] = adapter.getItem(index);
            } else if (index < 0) {
                visibles[counter] = "";
            } else if (index > adapter.getItemsCount() - 1) {
                visibles[counter] = "";
            } else {
                visibles[counter] = adapter.getItem(index);
            }

            counter++;

        }

        if (dividerType == DividerType.WRAP) {
            float startX;
            float endX;

            if (TextUtils.isEmpty(label)) {
                startX = (measuredWidth - maxTextWidth) / 2 - 12;
            } else {
                startX = (measuredWidth - maxTextWidth) / 4 - 12;
            }

            if (startX <= 0) {
                startX = 10;
            }
            endX = measuredWidth - startX;
            canvas.drawLine(startX, firstLineY, endX, firstLineY, paintIndicator);
            canvas.drawLine(startX, secondLineY, endX, secondLineY, paintIndicator);
        } else {
            canvas.drawLine(0.0F, firstLineY, measuredWidth, firstLineY, paintIndicator);
            canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintIndicator);
        }
        if (!TextUtils.isEmpty(label) && isCenterLabel) {
            int drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label);
            canvas.drawText(label, drawRightContentStart - CENTERCONTENTOFFSET, centerY, paintCenterText);
        }

        counter = 0;
        while (counter < itemsVisible) {
            canvas.save();
            double radian = ((itemHeight * counter - itemHeightOffset)) / radius;

            float angle = (float) (90D - (radian / Math.PI) * 180D);

            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {

                String contentText;

                if (!isCenterLabel && !TextUtils.isEmpty(label) && !TextUtils.isEmpty(getContentText(visibles[counter]))) {
                    contentText = getContentText(visibles[counter]) + label;
                } else {
                    contentText = getContentText(visibles[counter]);
                }

                reMeasureTextSize(contentText);

                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                float translateY = (float) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    float Y = maxTextHeight - CENTERCONTENTOFFSET;
                    canvas.drawText(contentText, drawCenterContentStart, Y, paintCenterText);

                    int preSelectedItem = adapter.indexOf(visibles[counter]);

                    selectedItem = preSelectedItem;

                } else {
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                }
                canvas.restore();
                paintCenterText.setTextSize(textSize);
            }
            counter++;
        }
    }


    private void reMeasureTextSize(String contentText) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
        int width = rect.width();
        int size = textSize;
        while (width > measuredWidth) {
            size--;
            paintCenterText.setTextSize(size);
            paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
            width = rect.width();
        }
        paintOuterText.setTextSize(size);
    }

    private int getLoopMappingIndex(int index) {
        if (index < 0) {
            index = index + adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        } else if (index > adapter.getItemsCount() - 1) {
            index = index - adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        }
        return index;
    }


    private String getContentText(Object item) {
        if (item == null) {
            return "";
        } else if (item instanceof IPickerViewData) {
            return ((IPickerViewData) item).getPickerViewText();
        } else if (item instanceof Integer) {
            return String.format(Locale.getDefault(), "%02d", (int) item);
        }
        return item.toString();
    }

    private void measuredCenterContentStart(String content) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            case Gravity.CENTER:
                if (isOptions || label == null || label.equals("") || !isCenterLabel) {
                    drawCenterContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                } else {
                    drawCenterContentStart = (int) ((measuredWidth - rect.width()) * 0.25);
                }
                break;
            case Gravity.LEFT:
                drawCenterContentStart = 0;
                break;
            case Gravity.RIGHT:
                drawCenterContentStart = measuredWidth - rect.width() - (int) CENTERCONTENTOFFSET;
                break;
        }
    }

    private void measuredOutContentStart(String content) {
        Rect rect = new Rect();
        paintOuterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            case Gravity.CENTER:
                if (isOptions || label == null || label.equals("") || !isCenterLabel) {
                    drawOutContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                } else {
                    drawOutContentStart = (int) ((measuredWidth - rect.width()) * 0.25);
                }
                break;
            case Gravity.LEFT:
                drawOutContentStart = 0;
                break;
            case Gravity.RIGHT:
                drawOutContentStart = measuredWidth - rect.width() - (int) CENTERCONTENTOFFSET;
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                float dy = previousY - event.getRawY();
                previousY = event.getRawY();
                totalScrollY = totalScrollY + dy;

                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (adapter.getItemsCount() - 1 - initPosition) * itemHeight;


                    if (totalScrollY - itemHeight * 0.25 < top) {
                        top = totalScrollY - dy;
                    } else if (totalScrollY + itemHeight * 0.25 > bottom) {
                        bottom = totalScrollY - dy;
                    }

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

            default:
                if (!eventConsumed) {

                    /**
                     * TODO<关于弧长的计算>
                     *
                     * 弧长公式： L = α*R
                     * 反余弦公式：arccos(cosα) = α
                     * 由于之前是有顺时针偏移90度，
                     * 所以实际弧度范围α2的值 ：α2 = π/2-α    （α=[0,π] α2 = [-π/2,π/2]）
                     * 根据正弦余弦转换公式 cosα = sin(π/2-α)
                     * 代入，得： cosα = sin(π/2-α) = sinα2 = (R - y) / R
                     * 所以弧长 L = arccos(cosα)*R = arccos((R - y) / R)*R
                     */

                    float y = event.getY();
                    double L = Math.acos((radius - y) / radius) * radius;

                    int circlePosition = (int) ((L + itemHeight / 2) / itemHeight);
                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;

                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {

                        smoothScroll(ACTION.DAGGLE);
                    } else {

                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }

        invalidate();
        return true;
    }


    public int getItemsCount() {
        return adapter != null ? adapter.getItemsCount() : 0;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void isCenterLabel(Boolean isCenterLabel) {
        this.isCenterLabel = isCenterLabel;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public void setIsOptions(boolean options) {
        isOptions = options;
    }


    public void setTextColorOut(int textColorOut) {
        if (textColorOut != 0) {
            this.textColorOut = textColorOut;
            paintOuterText.setColor(this.textColorOut);
        }
    }

    public void setTextColorCenter(int textColorCenter) {
        if (textColorCenter != 0) {

            this.textColorCenter = textColorCenter;
            paintCenterText.setColor(this.textColorCenter);
        }
    }

    public void setDividerColor(int dividerColor) {
        if (dividerColor != 0) {
            this.dividerColor = dividerColor;
            paintIndicator.setColor(this.dividerColor);
        }
    }

    public void setDividerType(DividerType dividerType) {
        this.dividerType = dividerType;
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier != 0) {


            this.lineSpacingMultiplier = lineSpacingMultiplier;
            judgeLineSpae();

        }
    }


    public ACTION getAction_now() {
        return action_now;
    }


}