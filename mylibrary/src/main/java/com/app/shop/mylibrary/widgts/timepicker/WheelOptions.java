package com.app.shop.mylibrary.widgts.timepicker;

import android.graphics.Typeface;
import android.view.View;


import com.app.shop.mylibrary.R;

import java.util.List;

public class WheelOptions<T> {
    private View view;
    private WheelView wv_option1;
    private WheelView wv_option2;
    private WheelView wv_option3;

    private List<T> mOptions1Items;
    private List<List<T>> mOptions2Items;
    private List<T> N_mOptions2Items;
    private List<List<List<T>>> mOptions3Items;
    private List<T> N_mOptions3Items;
    private boolean linkage;
    private OnItemSelectedListener wheelListener_option1;
    private OnItemSelectedListener wheelListener_option2;

    int textColorOut;
    int textColorCenter;
    int dividerColor;

    private WheelView.DividerType dividerType;

    float lineSpacingMultiplier = 1.6F;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public WheelOptions(View view, Boolean linkage) {
        super();
        this.linkage = linkage;
        this.view = view;
        wv_option1 = (WheelView) view.findViewById(R.id.options1);
        wv_option2 = (WheelView) view.findViewById(R.id.options2);
        wv_option3 = (WheelView) view.findViewById(R.id.options3);
    }


    public void setPicker(List<T> options1Items,
                          List<List<T>> options2Items,
                          List<List<List<T>>> options3Items) {
        this.mOptions1Items = options1Items;
        this.mOptions2Items = options2Items;
        this.mOptions3Items = options3Items;
        int len = ArrayWheelAdapter.DEFAULT_LENGTH;
        if (this.mOptions3Items == null)
            len = 8;
        if (this.mOptions2Items == null)
            len = 12;
        // option 1
        wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));
        wv_option1.setCurrentItem(0);
        // option2
        if (mOptions2Items != null)
            wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(0)));
        wv_option2.setCurrentItem(wv_option1.getCurrentItem());
        // option3
        if (mOptions3Items != null)
            wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(0).get(0)));
        wv_option3.setCurrentItem(wv_option3.getCurrentItem());
        wv_option1.setIsOptions(true);
        wv_option2.setIsOptions(true);
        wv_option3.setIsOptions(true);

        if (this.mOptions2Items == null) {
            wv_option2.setVisibility(View.GONE);
        } else {
            wv_option2.setVisibility(View.VISIBLE);
        }
        if (this.mOptions3Items == null) {
            wv_option3.setVisibility(View.GONE);
        } else {
            wv_option3.setVisibility(View.VISIBLE);
        }

        wheelListener_option1 = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                int opt2Select = 0;
                if (mOptions2Items != null) {
                    opt2Select = wv_option2.getCurrentItem();
                    opt2Select = opt2Select >= mOptions2Items.get(index).size() - 1 ? mOptions2Items.get(index).size() - 1 : opt2Select;

                    wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(index)));
                    wv_option2.setCurrentItem(opt2Select);
                }
                if (mOptions3Items != null) {
                    wheelListener_option2.onItemSelected(opt2Select);
                }
            }
        };
        wheelListener_option2 = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                if (mOptions3Items != null) {
                    int opt1Select = wv_option1.getCurrentItem();
                    opt1Select = opt1Select >= mOptions3Items.size() - 1 ? mOptions3Items.size() - 1 : opt1Select;
                    index = index >= mOptions2Items.get(opt1Select).size() - 1 ? mOptions2Items.get(opt1Select).size() - 1 : index;
                    int opt3 = wv_option3.getCurrentItem();

                    opt3 = opt3 >= mOptions3Items.get(opt1Select).get(index).size() - 1 ? mOptions3Items.get(opt1Select).get(index).size() - 1 : opt3;

                    wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(wv_option1.getCurrentItem()).get(index)));
                    wv_option3.setCurrentItem(opt3);

                }
            }
        };

        if (options2Items != null && linkage)
            wv_option1.setOnItemSelectedListener(wheelListener_option1);
        if (options3Items != null && linkage)
            wv_option2.setOnItemSelectedListener(wheelListener_option2);
    }


    public void setNPicker(List<T> options1Items,
                           List<T> options2Items,
                           List<T> options3Items) {
        this.mOptions1Items = options1Items;
        this.N_mOptions2Items = options2Items;
        this.N_mOptions3Items = options3Items;
        int len = ArrayWheelAdapter.DEFAULT_LENGTH;
        if (this.N_mOptions3Items == null)
            len = 8;
        if (this.N_mOptions2Items == null)
            len = 12;
        // option1
        wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));
        wv_option1.setCurrentItem(0);
        // option2
        if (N_mOptions2Items != null)
            wv_option2.setAdapter(new ArrayWheelAdapter(N_mOptions2Items));
        wv_option2.setCurrentItem(wv_option1.getCurrentItem());
        // option3
        if (N_mOptions3Items != null)
            wv_option3.setAdapter(new ArrayWheelAdapter(N_mOptions3Items));
        wv_option3.setCurrentItem(wv_option3.getCurrentItem());
        wv_option1.setIsOptions(true);
        wv_option2.setIsOptions(true);
        wv_option3.setIsOptions(true);

        if (this.N_mOptions2Items == null) {
            wv_option2.setVisibility(View.GONE);
        } else {
            wv_option2.setVisibility(View.VISIBLE);
        }
        if (this.N_mOptions3Items == null) {
            wv_option3.setVisibility(View.GONE);
        } else {
            wv_option3.setVisibility(View.VISIBLE);
        }
    }


    public void setTextContentSize(int textSize) {
        wv_option1.setTextSize(textSize);
        wv_option2.setTextSize(textSize);
        wv_option3.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wv_option1.setTextColorOut(textColorOut);
        wv_option2.setTextColorOut(textColorOut);
        wv_option3.setTextColorOut(textColorOut);

    }

    private void setTextColorCenter() {
        wv_option1.setTextColorCenter(textColorCenter);
        wv_option2.setTextColorCenter(textColorCenter);
        wv_option3.setTextColorCenter(textColorCenter);

    }

    private void setDividerColor() {
        wv_option1.setDividerColor(dividerColor);
        wv_option2.setDividerColor(dividerColor);
        wv_option3.setDividerColor(dividerColor);
    }

    private void setDividerType() {
        wv_option1.setDividerType(dividerType);
        wv_option2.setDividerType(dividerType);
        wv_option3.setDividerType(dividerType);
    }

    private void setLineSpacingMultiplier() {
        wv_option1.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_option2.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_option3.setLineSpacingMultiplier(lineSpacingMultiplier);

    }


    public void setLabels(String label1, String label2, String label3) {
        if (label1 != null)
            wv_option1.setLabel(label1);
        if (label2 != null)
            wv_option2.setLabel(label2);
        if (label3 != null)
            wv_option3.setLabel(label3);
    }

    public void setTextXOffset(int xoffset_one, int xoffset_two, int xoffset_three){

    }


    public void setCyclic(boolean cyclic) {
        wv_option1.setCyclic(cyclic);
        wv_option2.setCyclic(cyclic);
        wv_option3.setCyclic(cyclic);
    }

    public void setTypeface(Typeface font) {
        wv_option1.setTypeface(font);
        wv_option2.setTypeface(font);
        wv_option3.setTypeface(font);
    }

    public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
        wv_option1.setCyclic(cyclic1);
        wv_option2.setCyclic(cyclic2);
        wv_option3.setCyclic(cyclic3);
    }

    public int[] getCurrentItems() {
        int[] currentItems = new int[3];
        currentItems[0] = wv_option1.getCurrentItem();

        if (mOptions2Items != null && mOptions2Items.size() > 0) {//非空判断
            currentItems[1] = wv_option2.getCurrentItem() > (mOptions2Items.get(currentItems[0]).size() - 1) ? 0 : wv_option2.getCurrentItem();
        } else {
            currentItems[1] = wv_option2.getCurrentItem();
        }

        if (mOptions3Items != null && mOptions3Items.size() > 0) {//非空判断
            currentItems[2] = wv_option3.getCurrentItem() > (mOptions3Items.get(currentItems[0]).get(currentItems[1]).size() - 1) ? 0 : wv_option3.getCurrentItem();
        } else {
            currentItems[2] = wv_option3.getCurrentItem();
        }

        return currentItems;
    }

    public void setCurrentItems(int option1, int option2, int option3) {
        if (linkage) {
            itemSelected(option1, option2, option3);
        }
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
        wv_option3.setCurrentItem(option3);
    }

    private void itemSelected(int opt1Select, int opt2Select, int opt3Select) {
        if (mOptions2Items != null) {
            wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(opt1Select)));
            wv_option2.setCurrentItem(opt2Select);
        }
        if (mOptions3Items != null) {
            wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(opt1Select).get(opt2Select)));
            wv_option3.setCurrentItem(opt3Select);
        }
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    public void isCenterLabel(Boolean isCenterLabel) {
        wv_option1.isCenterLabel(isCenterLabel);
        wv_option2.isCenterLabel(isCenterLabel);
        wv_option3.isCenterLabel(isCenterLabel);
    }

}
