package com.app.shop.mylibrary.widgts.timepicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.shop.mylibrary.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TimePickerView extends BasePickerView implements View.OnClickListener, WheelTime.RollDay {
    private int layoutRes;
    private CustomListener customListener;

    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR, MONTH_DAY_HOUR_MIN, YEAR_MONTH, YEAR_MONTH_DAY_HOUR_MIN, YEAR_MONTH_DAY_HOUR
    }

    WheelTime wheelTime;
    private Button btnSubmit, btnCancel;
    private TextView tvTitle;
    private OnTimeSelectListener timeSelectListener;
    private int gravity = Gravity.CENTER;
    private Type type;

    private WarpLinearLayout mygroup;

    private String Str_Submit;
    private String Str_Cancel;
    private String Str_Title;

    private int Color_Submit;
    private int Color_Cancel;
    private int Color_Title;

    private int Color_Background_Wheel;
    private int Color_Background_Title;

    private Drawable Drawable_Background_Title;

    private int Size_Submit_Cancel;
    private int Size_Title;
    private int Size_Content;

    private Calendar date;
    private Calendar startDate;
    private Calendar endDate;
    private int startYear;
    private int endYear;

    private boolean ycyclic, mcyclic, dcyclic;
    private boolean cancelable;
    private boolean isCenterLabel;

    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;
    private int backgroudId;


    private float lineSpacingMultiplier = 1.6F;
    private boolean isDialog;
    private String label_year, label_month, label_day, label_hours, label_mins, label_seconds;
    private WheelView.DividerType dividerType;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private Context mContext;


    private String selectIdcard = "";


    public TimePickerView(Builder builder) {
        super(builder.context);
        this.timeSelectListener = builder.timeSelectListener;
        this.gravity = builder.gravity;
        this.type = builder.type;
        this.Str_Submit = builder.Str_Submit;
        this.Str_Cancel = builder.Str_Cancel;
        this.Str_Title = builder.Str_Title;
        this.Color_Submit = builder.Color_Submit;
        this.Color_Cancel = builder.Color_Cancel;
        this.Color_Title = builder.Color_Title;
        this.Color_Background_Wheel = builder.Color_Background_Wheel;
        this.Color_Background_Title = builder.Color_Background_Title;
        this.Drawable_Background_Title = builder.Drawable_Background_Title;
        this.Size_Submit_Cancel = builder.Size_Submit_Cancel;
        this.Size_Title = builder.Size_Title;
        this.Size_Content = builder.Size_Content;
        this.startYear = builder.startYear;
        this.endYear = builder.endYear;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.date = builder.date;
        this.ycyclic = builder.ycyclic;
        this.mcyclic = builder.mcyclic;
        this.dcyclic = builder.dcyclic;
        this.isCenterLabel = builder.isCenterLabel;
        this.cancelable = builder.cancelable;
        this.label_year = builder.label_year;
        this.label_month = builder.label_month;
        this.label_day = builder.label_day;
        this.label_hours = builder.label_hours;
        this.label_mins = builder.label_mins;
        this.label_seconds = builder.label_seconds;
        this.textColorCenter = builder.textColorCenter;
        this.textColorOut = builder.textColorOut;
        this.dividerColor = builder.dividerColor;
        this.customListener = builder.customListener;
        this.layoutRes = builder.layoutRes;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.isDialog = builder.isDialog;
        this.dividerType = builder.dividerType;
        this.backgroudId = builder.backgroudId;
        this.decorView = builder.decorView;
        this.mContext = builder.context;
        initView(builder.context);


    }


    public static class Builder {
        private int layoutRes = R.layout.pickerview_time;
        private CustomListener customListener;
        private Context context;
        private OnTimeSelectListener timeSelectListener;

        private Type type = Type.ALL;
        private int gravity = Gravity.CENTER;

        private String Str_Submit;
        private String Str_Cancel;
        private String Str_Title;

        private int Color_Submit;
        private int Color_Cancel;
        private int Color_Title;

        private int Color_Background_Wheel;
        private int Color_Background_Title;

        private Drawable Drawable_Background_Title;

        private int Size_Submit_Cancel = 17;
        private int Size_Title = 18;
        private int Size_Content = 18;
        private Calendar date;
        private Calendar startDate;
        private Calendar endDate;
        private int startYear;
        private int endYear;

        private boolean ycyclic = false;
        private boolean mcyclic = false;
        private boolean dcyclic = false;
        private boolean cancelable = true;
        private boolean isCenterLabel = true;
        public ViewGroup decorView;
        private int textColorOut;
        private int textColorCenter;
        private int dividerColor;
        private int backgroudId;
        private WheelView.DividerType dividerType;

        private float lineSpacingMultiplier = 1.6F;

        private boolean isDialog;


        private String label_year, label_month, label_day, label_hours, label_mins, label_seconds;

        //Required
        public Builder(Context context, OnTimeSelectListener listener) {
            this.context = context;
            this.timeSelectListener = listener;
        }

        //Option
        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setSubmitText(String Str_Submit) {
            this.Str_Submit = Str_Submit;
            return this;
        }


        public Builder isDialog(boolean isDialog) {
            this.isDialog = isDialog;
            return this;
        }

        public Builder setCancelText(String Str_Cancel) {
            this.Str_Cancel = Str_Cancel;
            return this;
        }

        public Builder setTitleText(String Str_Title) {
            this.Str_Title = Str_Title;
            return this;
        }

        public Builder setSubmitColor(int Color_Submit) {
            this.Color_Submit = Color_Submit;
            return this;
        }

        public Builder setCancelColor(int Color_Cancel) {
            this.Color_Cancel = Color_Cancel;
            return this;
        }


        public Builder setDecorView(ViewGroup decorView) {
            this.decorView = decorView;
            return this;
        }

        public Builder setBgColor(int Color_Background_Wheel) {
            this.Color_Background_Wheel = Color_Background_Wheel;
            return this;
        }

        public Builder setTitleBgColor(int Color_Background_Title) {
            this.Color_Background_Title = Color_Background_Title;
            return this;
        }

        public Builder setDrawable_Background_Title(Drawable drawable_Background_Title) {
            Drawable_Background_Title = drawable_Background_Title;
            return this;
        }

        public Builder setTitleColor(int Color_Title) {
            this.Color_Title = Color_Title;
            return this;
        }

        public Builder setSubCalSize(int Size_Submit_Cancel) {
            this.Size_Submit_Cancel = Size_Submit_Cancel;
            return this;
        }

        public Builder setTitleSize(int Size_Title) {
            this.Size_Title = Size_Title;
            return this;
        }

        public Builder setContentSize(int Size_Content) {
            this.Size_Content = Size_Content;
            return this;
        }

        public Builder setDate(Calendar date) {
            this.date = date;
            return this;
        }

        public Builder setLayoutRes(int res, CustomListener customListener) {
            this.layoutRes = res;
            this.customListener = customListener;
            return this;
        }

        public Builder setRange(int startYear, int endYear) {
            this.startYear = startYear;
            this.endYear = endYear;
            return this;
        }



        public Builder setRangDate(Calendar startDate, Calendar endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            return this;
        }


        public Builder setLineSpacingMultiplier(float lineSpacingMultiplier) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            return this;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder setDividerType(WheelView.DividerType dividerType) {
            this.dividerType = dividerType;
            return this;
        }

        public Builder setBackgroudId(int backgroudId) {
            this.backgroudId = backgroudId;
            return this;
        }


        public Builder setTextColorCenter(int textColorCenter) {
            this.textColorCenter = textColorCenter;
            return this;
        }

        public Builder setTextColorOut(int textColorOut) {
            this.textColorOut = textColorOut;
            return this;
        }

        public Builder isCyclic(boolean ycyclic, boolean mcyclic, boolean dcyclic) {
            this.ycyclic = ycyclic;
            this.mcyclic = mcyclic;
            this.dcyclic = dcyclic;
            return this;
        }

        public Builder setOutSideCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setLabel(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
            this.label_year = label_year;
            this.label_month = label_month;
            this.label_day = label_day;
            this.label_hours = label_hours;
            this.label_mins = label_mins;
            this.label_seconds = label_seconds;
            return this;
        }

        public Builder isCenterLabel(boolean isCenterLabel) {
            this.isCenterLabel = isCenterLabel;
            return this;
        }


        public TimePickerView build() {
            return new TimePickerView(this);
        }
    }


    private void initView(Context context) {
        setDialogOutSideCancelable(cancelable);
        initViews(backgroudId);
        init();
        initEvents();
        if (customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);

            mygroup = (WarpLinearLayout) findViewById(R.id.mygroup);

            tvTitle = (TextView) findViewById(R.id.tvTitle);

            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            btnCancel = (Button) findViewById(R.id.btnCancel);

            btnSubmit.setTag(TAG_SUBMIT);
            btnCancel.setTag(TAG_CANCEL);

            btnSubmit.setOnClickListener(this);
            btnCancel.setOnClickListener(this);

            btnSubmit.setText(TextUtils.isEmpty(Str_Submit) ? context.getResources().getString(R.string.pickerview_submit) : Str_Submit);
            btnCancel.setText(TextUtils.isEmpty(Str_Cancel) ? context.getResources().getString(R.string.pickerview_cancel) : Str_Cancel);
            tvTitle.setText(TextUtils.isEmpty(Str_Title) ? "" : Str_Title);

            btnSubmit.setTextColor(Color_Submit == 0 ? pickerview_timebtn_nor : Color_Submit);
            btnCancel.setTextColor(Color_Cancel == 0 ? pickerview_timebtn_nor : Color_Cancel);
            tvTitle.setTextColor(Color_Title == 0 ? pickerview_topbar_title : Color_Title);

            btnSubmit.setTextSize(Size_Submit_Cancel);
            btnCancel.setTextSize(Size_Submit_Cancel);
            tvTitle.setTextSize(Size_Title);

            RelativeLayout rv_top_bar = (RelativeLayout) findViewById(R.id.rv_topbar);
            rv_top_bar.setBackgroundColor(Color_Background_Title == 0 ? pickerview_bg_topbar : Color_Background_Title);

            rv_top_bar.setBackgroundDrawable(Drawable_Background_Title);


        } else {
            customListener.customLayout(LayoutInflater.from(context).inflate(layoutRes, contentContainer));
        }

        LinearLayout timePickerView = (LinearLayout) findViewById(R.id.timepicker);

        timePickerView.setBackgroundColor(Color_Background_Wheel == 0 ? bgColor_default : Color_Background_Wheel);

        wheelTime = new WheelTime(timePickerView, type, gravity, Size_Content, this);

        if (startYear != 0 && endYear != 0 && startYear <= endYear) {
            setRange();
        }

        if (startDate != null && endDate != null) {
            if (startDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
                setRangDate();
            }
        } else if (startDate != null && endDate == null) {
            setRangDate();
        } else if (startDate == null && endDate != null) {
            setRangDate();
        }

        setTime();
        wheelTime.setLabels(label_year, label_month, label_day, label_hours, label_mins, label_seconds);

        setOutSideCancelable(cancelable);
        wheelTime.setCyclic(ycyclic, mcyclic, dcyclic);
        wheelTime.setDividerColor(dividerColor);
        wheelTime.setDividerType(dividerType);
        wheelTime.setLineSpacingMultiplier(lineSpacingMultiplier);
        wheelTime.setTextColorOut(textColorOut);
        wheelTime.setTextColorCenter(textColorCenter);
        wheelTime.isCenterLabel(isCenterLabel);
    }


    public void setDate(Calendar date) {
        this.date = date;
        setTime();
    }


    private void setRange() {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);

    }


    private void setRangDate() {
        wheelTime.setRangDate(startDate, endDate);
        if (startDate != null && endDate != null) {
            if (date == null || date.getTimeInMillis() < startDate.getTimeInMillis()
                    || date.getTimeInMillis() > endDate.getTimeInMillis()) {
                date = startDate;
            }
        } else if (startDate != null) {
            date = startDate;
        } else if (endDate != null) {
            date = endDate;
        }
    }

    private void setTime() {
        int year, month, day, hours, minute, seconds;

        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DAY_OF_MONTH);
            hours = date.get(Calendar.HOUR_OF_DAY);
            minute = date.get(Calendar.MINUTE);
            seconds = date.get(Calendar.SECOND);
        }


        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            if (wheelTime.isSelectFinish()) {
                returnData();
            }
        }
        dismiss();
    }

    public void returnData() {
        if (timeSelectListener != null) {
            try {
                Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                timeSelectListener.onTimeSelect(date, clickView, selectIdcard);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(Date date, View v, String idcard);
    }

    @Override
    public boolean isDialog() {
        return isDialog;
    }


    @Override
    public void resultRoll() {

        Date date = null;
        try {
            date = WheelTime.dateFormat.parse(wheelTime.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
