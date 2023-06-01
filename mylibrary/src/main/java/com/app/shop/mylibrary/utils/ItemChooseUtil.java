package com.app.shop.mylibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.app.shop.mylibrary.R;
import com.app.shop.mylibrary.interfaces.I_itemSelectedListener;
import com.app.shop.mylibrary.widgts.MyAlertDialog;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.List;

public class ItemChooseUtil {

    public static void showItemWheel(Context context, final List list, String title, int position, final I_itemSelectedListener i_itemSelectedListener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.loopview_item, null);
        final LoopView loopView = (LoopView) contentView.findViewById(R.id.loopview);
        loopView.setNotLoop();//set loop
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
            }
        });
        loopView.setDividerColor(Color.parseColor("#E3E3E3"));
        loopView.setCenterTextColor(Color.parseColor("#43496a"));
        loopView.setItems(list);//set initial data
        loopView.setCurrentPosition(position); //set initial position
        loopView.setTextSize(15);//set font size
        final MyAlertDialog dialog1 = new MyAlertDialog(context)
                .builder()
                .setView(contentView);
        dialog1.setTitle(title);
        dialog1.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog1.setPositiveButton("Confirm", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_itemSelectedListener.onItemSelected(loopView.getSelectedItem());
            }
        });
        dialog1.show();
    }
}

