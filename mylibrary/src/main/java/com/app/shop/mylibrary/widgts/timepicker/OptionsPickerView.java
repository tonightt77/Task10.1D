package com.app.shop.mylibrary.widgts.timepicker;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.app.shop.mylibrary.R;

import java.util.List;

public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener {

    WheelOptions<T> wheelOptions;
    private int layoutRes;
    private CustomListener customListener;
    private Button btnSubmit, btnCancel;
    private TextView tvTitle;
    private RelativeLayout rv_top_bar;

    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    private OnOptionsSelectListener optionsSelectListener;

    private String Str_Submit;
    private String Str_Cancel;
    private String Str_Title;

    private int Color_Submit;
    private int Color_Cancel;
    private int Color_Title;

    private int Color_Background_Wheel;
    private int Color_Background_Title;

    private int Size_Submit_Cancel;
    private int Size_Title;
    private int Size_Content;

    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;
    private int backgroundId;

    private float lineSpacingMultiplier = 1.6F;
    private boolean isDialog;

    private boolean cancelable;
    private boolean linkage;

    private boolean isCenterLabel;

    private String label1;
    private String label2;
    private String label3;

    private boolean cyclic1;
    private boolean cyclic2;
    private boolean cyclic3;

    private Typeface font;

    private int option1;
    private int option2;
    private int option3;

    private int xoffset_one;
    private int xoffset_two;
    private int xoffset_three;

    private WheelView.DividerType dividerType;

    public OptionsPickerView(Builder builder) {
        super(builder.context);
        this.optionsSelectListener = builder.optionsSelectListener;
        this.Str_Submit = builder.Str_Submit;
        this.Str_Cancel = builder.Str_Cancel;
        this.Str_Title = builder.Str_Title;

        this.Color_Submit = builder.Color_Submit;
        this.Color_Cancel = builder.Color_Cancel;
        this.Color_Title = builder.Color_Title;
        this.Color_Background_Wheel = builder.Color_Background_Wheel;
        this.Color_Background_Title = builder.Color_Background_Title;

        this.Size_Submit_Cancel = builder.Size_Submit_Cancel;
        this.Size_Title = builder.Size_Title;
        this.Size_Content = builder.Size_Content;

        this.cyclic1 = builder.cyclic1;
        this.cyclic2 = builder.cyclic2;
        this.cyclic3 = builder.cyclic3;

        this.cancelable = builder.cancelable;
        this.linkage = builder.linkage;
        this.isCenterLabel = builder.isCenterLabel;

        this.label1 = builder.label1;
        this.label2 = builder.label2;
        this.label3 = builder.label3;

        this.font = builder.font;

        this.option1 = builder.option1;
        this.option2 = builder.option2;
        this.option3 = builder.option3;
        this.xoffset_one = builder.xoffset_one;
        this.xoffset_two = builder.xoffset_two;
        this.xoffset_three = builder.xoffset_three;

        this.textColorCenter = builder.textColorCenter;
        this.textColorOut = builder.textColorOut;
        this.dividerColor = builder.dividerColor;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.customListener = builder.customListener;
        this.layoutRes = builder.layoutRes;
        this.isDialog = builder.isDialog;
        this.dividerType = builder.dividerType;
        this.backgroundId = builder.backgroundId;
        this.decorView = builder.decorView;
        initView(builder.context);
    }


    public static class Builder {
        private int layoutRes = R.layout.pickerview_options;
        private CustomListener customListener;
        private Context context;
        private OnOptionsSelectListener optionsSelectListener;

        private String Str_Submit;
        private String Str_Cancel;
        private String Str_Title;

        private int Color_Submit;
        private int Color_Cancel;
        private int Color_Title;

        private int Color_Background_Wheel;
        private int Color_Background_Title;

        private int Size_Submit_Cancel = 17;
        private int Size_Title = 18;
        private int Size_Content = 18;

        private boolean cancelable = true;
        private boolean linkage = true;
        private boolean isCenterLabel = true;

        private int textColorOut;
        private int textColorCenter;
        private int dividerColor;
        private int backgroundId;
        public ViewGroup decorView;

        private float lineSpacingMultiplier = 1.6F;
        private boolean isDialog;

        private String label1;
        private String label2;
        private String label3;

        private boolean cyclic1 = false;
        private boolean cyclic2 = false;
        private boolean cyclic3 = false;

        private Typeface font;

        private int option1;
        private int option2;
        private int option3;

        private int xoffset_one;
        private int xoffset_two;
        private int xoffset_three;

        private WheelView.DividerType dividerType;

        //Required
        public Builder(Context context, OnOptionsSelectListener listener) {
            this.context = context;
            this.optionsSelectListener = listener;
        }

        //Option

        public Builder setSubmitText(String Str_Cancel) {
            this.Str_Submit = Str_Cancel;
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

        public Builder isDialog(boolean isDialog) {
            this.isDialog = isDialog;
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

        public Builder setBackgroundId(int backgroundId) {
            this.backgroundId = backgroundId;
            return this;
        }


        public Builder setDecorView(ViewGroup decorView) {
            this.decorView = decorView;
            return this;
        }

        public Builder setLayoutRes(int res, CustomListener listener) {
            this.layoutRes = res;
            this.customListener = listener;
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

        public Builder setContentTextSize(int Size_Content) {
            this.Size_Content = Size_Content;
            return this;
        }

        public Builder setOutSideCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }


        @Deprecated
        public Builder setLinkage(boolean linkage) {
            this.linkage = linkage;
            return this;
        }

        public Builder setLabels(String label1, String label2, String label3) {
            this.label1 = label1;
            this.label2 = label2;
            this.label3 = label3;
            return this;
        }


        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }


        public Builder setTextColorCenter(int textColorCenter) {
            this.textColorCenter = textColorCenter;
            return this;
        }


        public Builder setSelectOptions(int option1, int option2) {
            this.option1 = option1;
            this.option2 = option2;
            return this;
        }


        public Builder isCenterLabel(boolean isCenterLabel) {
            this.isCenterLabel = isCenterLabel;
            return this;
        }

        public OptionsPickerView build() {
            return new OptionsPickerView(this);
        }
    }


    private void initView(Context context) {
        setDialogOutSideCancelable(cancelable);
        initViews(backgroundId);
        init();
        initEvents();
        if (customListener == null) {
            LayoutInflater.from(context).inflate(layoutRes, contentContainer);


            tvTitle = (TextView) findViewById(R.id.tvTitle);
            rv_top_bar = (RelativeLayout) findViewById(R.id.rv_topbar);


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
            rv_top_bar.setBackgroundColor(Color_Background_Title == 0 ? pickerview_bg_topbar : Color_Background_Title);


            btnSubmit.setTextSize(Size_Submit_Cancel);
            btnCancel.setTextSize(Size_Submit_Cancel);
            tvTitle.setTextSize(Size_Title);
            tvTitle.setText(Str_Title);
        } else {
            customListener.customLayout(LayoutInflater.from(context).inflate(layoutRes, contentContainer));
        }


        final LinearLayout optionsPicker = (LinearLayout) findViewById(R.id.optionspicker);
        optionsPicker.setBackgroundColor(Color_Background_Wheel == 0 ? bgColor_default : Color_Background_Wheel);

        wheelOptions = new WheelOptions(optionsPicker, linkage);
        wheelOptions.setTextContentSize(Size_Content);
        wheelOptions.setLabels(label1, label2, label3);
        wheelOptions.setTextXOffset(xoffset_one, xoffset_two, xoffset_three);

        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
        wheelOptions.setTypeface(font);

        setOutSideCancelable(cancelable);

        if (tvTitle != null) {
            tvTitle.setText(Str_Title);
        }

        wheelOptions.setDividerColor(dividerColor);
        wheelOptions.setDividerType(dividerType);
        wheelOptions.setLineSpacingMultiplier(lineSpacingMultiplier);
        wheelOptions.setTextColorOut(textColorOut);
        wheelOptions.setTextColorCenter(textColorCenter);
        wheelOptions.isCenterLabel(isCenterLabel);
    }


    private void SetCurrentItems() {
        if (wheelOptions != null) {
            wheelOptions.setCurrentItems(option1, option2, option3);
        }
    }

    public void setPicker(List<T> optionsItems) {
        this.setPicker(optionsItems, null, null);
    }

    public void setPicker(List<T> options1Items, List<List<T>> options2Items) {
        this.setPicker(options1Items, options2Items, null);
    }

    public void setPicker(List<T> options1Items,
                          List<List<T>> options2Items,
                          List<List<List<T>>> options3Items) {

        wheelOptions.setPicker(options1Items, options2Items, options3Items);
        SetCurrentItems();
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            returnData();
        }
        dismiss();
    }


    public void returnData() {
        if (optionsSelectListener != null) {
            int[] optionsCurrentItems = wheelOptions.getCurrentItems();
            optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2], clickView);
        }
    }

    public interface OnOptionsSelectListener {
        void onOptionsSelect(int options1, int options2, int options3, View v);
    }

    @Override
    public boolean isDialog() {
        return isDialog;
    }
}
