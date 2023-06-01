package com.app.shop.mylibrary.widgts.timepicker;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;


import com.app.shop.mylibrary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TimePickerUtil {

    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;

    private int order;
    private TimePickerView mYearAndMonth;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String mType) {
        type = mType;
    }


    public void setDatePicker(Context context, TimePickerView.Type type, Calendar selectedDate, Calendar startDate, Calendar endDate, final TimePickerCallback callback) {

        if (selectedDate == null) {
            selectedDate = Calendar.getInstance();
        }
        if (startDate == null) {
            startDate = Calendar.getInstance();
            startDate.set(1900, 1, 1);
        }


        if (endDate == null) {
            endDate = Calendar.getInstance();
            endDate.set(2100, 12, 29);
        }

        if (type == null) {
            type = TimePickerView.Type.YEAR_MONTH_DAY;
        }

        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v, String idcard) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                callback.setTimeCallback(order, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), idcard);

            }
        })
                .setType(type)
                .setCancelText("cancel")
                .setSubmitText("commit")
                .setSubCalSize(15)
                .setContentSize(18)
                .setTitleSize(18)
                .setTitleText("")
                .setOutSideCancelable(true)
                .isCyclic(false, false, false)
                .setTitleColor(Color.BLACK)
                .setSubmitColor(Color.parseColor("#0088ff"))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.WHITE)
                .setDrawable_Background_Title(context.getResources().getDrawable(R.drawable.shape_dialog_bottom_top))
                .setBgColor(Color.WHITE)
                .setRange(1900, 2050)
                .setDividerColor(Color.parseColor("#e3e3e3"))
                .setDate(selectedDate)
                .setTextColorCenter(Color.parseColor("#333333"))
                .setTextColorOut(Color.parseColor("#999999"))
                .setLineSpacingMultiplier(1.6f)
                .setRangDate(startDate, endDate)
                .setLabel("年", "M", "D", "", "", "")
                .isCenterLabel(true)
                .isDialog(false)
                .setDividerType(WheelView.DividerType.WRAP)
                .build();

    }


    public void initOptionPicker(Context context, List<String> options1Items, final OptionSelectistener listener) {//条件选择器初始化

        pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.onSelect(options1);
            }
        })
                .setTitleText("")
                .setContentTextSize(20)
                .setDividerColor(Color.LTGRAY)
                .setSelectOptions(0, 1)
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.WHITE)
                .setCancelColor(Color.BLACK)
                .setSubmitColor(Color.RED)
                .setTextColorCenter(Color.parseColor("#666666"))
                .isCenterLabel(false)
                .setLabels("", "", "")
                .setBackgroundId(Color.parseColor("#00000000"))
                .build();
        pvOptions.setPicker(options1Items);
    }

    public void showOpPicker() {
        if (pvOptions != null) {
            pvOptions.show();
        }
    }

    public void showTimePicker(int order) {
        switch (order) {
            case 0:
            case 1:
            case 3:
            case 5:
            case 9:
                if (pvTime != null) {
                    this.order = order;
                    pvTime.show();
                }
                break;
            default:
                if (mYearAndMonth != null) {
                    this.order = order;
                    mYearAndMonth.show();
                }
                break;
        }
    }


    public void setDatePickerYearAndMouth(Context context, Calendar selectedDate, Calendar startDate, Calendar endDate, final TimePickerCallback callback) {

        if (selectedDate == null) {
            selectedDate = Calendar.getInstance();
        }
        if (startDate == null) {
            startDate = Calendar.getInstance();
            startDate.set(1900, 0, 1);
        }

        if (endDate == null) {
            endDate = Calendar.getInstance();
            endDate.set(2100, 11, 29);
        }

        mYearAndMonth = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v, String idcard) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                callback.setTimeCallback(order, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 0, 0, idcard);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH)
                .setCancelText("cancel")
                .setSubmitText("commit")
                .setSubCalSize(15)
                .setContentSize(18)
                .setTitleSize(18)
                .setTitleText("")
                .setOutSideCancelable(false)
                .isCyclic(false, true, false)
                .setTitleColor(Color.BLACK)
                .setSubmitColor(Color.RED)
                .setCancelColor(Color.parseColor("#999999"))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setRange(1900, 2050)
                .setDividerColor(Color.parseColor("#FFFFFF"))
                .setDate(selectedDate)
                .setTextColorCenter(Color.parseColor("#666666"))
                .setTextColorOut(Color.parseColor("#999999"))
                .setLineSpacingMultiplier(1.6f)
                .setRangDate(startDate, endDate)
                .setLabel("年", "M", "D", "", "", "")
                .isCenterLabel(false)
                .isDialog(false)
                .setDividerType(WheelView.DividerType.WRAP)
                .build();
    }

    public void setInfoSettingPicker(Context context, Calendar selectedDate, Calendar startDate, Calendar endDate, final TimePickerCallback callback, ViewGroup ResLayout, int custermLayoutId, CustomListener listener) {

        if (selectedDate == null) {
            selectedDate = Calendar.getInstance();
        }
        if (startDate == null) {
            startDate = Calendar.getInstance();
            startDate.set(1900, 1, 1);
        }


        if (endDate == null) {
            endDate = Calendar.getInstance();
            endDate.set(3099, 12, 29);
        }

        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v, String idcard) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                callback.setTimeCallback(order, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 0, 0, idcard);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setCancelText("cancel")
                .setSubmitText("commit")
                .setSubCalSize(15)
                .setContentSize(18)
                .setTitleSize(18)
                .setTitleText("")
                .setOutSideCancelable(false)
                .isCyclic(false, true, true)
                .setTitleColor(Color.BLACK)
                .setSubmitColor(Color.RED)
                .setCancelColor(Color.parseColor("#999999"))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setRange(1900, 2050)
                .setDividerColor(Color.parseColor("#FFFFFF"))
                .setDate(selectedDate)
                .setTextColorCenter(Color.parseColor("#666666"))
                .setTextColorOut(Color.parseColor("#999999"))
                .setLineSpacingMultiplier(1.6f)
                .setRangDate(startDate, endDate)
                .setLabel("年", "M", "D", "", "", "")
                .isCenterLabel(false)
                .isDialog(false)
                .setDividerType(WheelView.DividerType.FILL)
                .setDecorView(ResLayout)
                .setLayoutRes(custermLayoutId, listener)
                .build();
    }
}
