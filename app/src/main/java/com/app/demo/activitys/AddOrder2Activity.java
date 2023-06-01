package com.app.demo.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.demo.AppManager;
import com.app.demo.R;
import com.app.demo.beans.OrderBean;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.beans.EventMessage;
import com.app.shop.mylibrary.interfaces.I_itemSelectedListener;
import com.app.shop.mylibrary.utils.ItemChooseUtil;
import com.app.shop.mylibrary.utils.StringUtil;
import com.app.shop.mylibrary.utils.ToastUtil;
import com.app.shop.mylibrary.utils.UserManager;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddOrder2Activity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_goods_type)
    TextView tv_goods_type;
    @BindView(R.id.edt_weight)
    EditText edt_weight;
    @BindView(R.id.edt_wight)
    EditText edt_wight;
    @BindView(R.id.edt_length)
    EditText edt_length;
    @BindView(R.id.edt_height)
    EditText edt_height;
    @BindView(R.id.tv_vehicle_type)
    TextView tv_vehicle_type;

    OrderBean bean;
    List<String> list_goods_type = new ArrayList<>();
    List<String> list_vehicle_type = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_2);
        ButterKnife.bind(this);
        tvTitle.setText("New delivery");
        Bundle bundle = getIntent().getExtras();
        bean = (OrderBean) bundle.getParcelable("bean");
        list_goods_type.add("Furniture");
        list_goods_type.add("Dry goods");
        list_goods_type.add("Food");
        list_goods_type.add("Building material");
        list_goods_type.add("Other");
        list_vehicle_type.add("Truck");
        list_vehicle_type.add("Van");
        list_vehicle_type.add("Refrigerated truck");
        list_vehicle_type.add("Mini-truck");
        list_vehicle_type.add("Other");
    }

    private void save() {

        String goodsType = tv_goods_type.getText().toString();
        String vehicleType = tv_vehicle_type.getText().toString();
        String weight = edt_weight.getText().toString();
        String wight = edt_wight.getText().toString();
        String length = edt_length.getText().toString();
        String height = edt_height.getText().toString();

        if (StringUtil.isEmpty(goodsType) || StringUtil.isEmpty(vehicleType) || StringUtil.isEmpty(weight)
                || StringUtil.isEmpty(wight) || StringUtil.isEmpty(length) || StringUtil.isEmpty(height)) {
            ToastUtil.showToast(this, "Order details cannot be empty");
            return;
        }
        bean.setGoodsType(goodsType);
        bean.setVehicleType(vehicleType);
        bean.setWeight(weight);
        bean.setWight(wight);
        bean.setLength(length);
        bean.setHeight(height);
        bean.setUser_id(UserManager.getUserId(this));
        bean.setUser_name(UserManager.getUserName(this));
        bean.save();

        EventBus.getDefault().post(new EventMessage(EventMessage.refresh));
        onBackPressed();
    }


    @OnClick({R.id.imgv_return, R.id.tv_goods_type, R.id.tv_vehicle_type, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_return:
                onBackPressed();
                break;
            case R.id.tv_goods_type:
                ItemChooseUtil.showItemWheel(this, list_goods_type, "Type", 0, new I_itemSelectedListener() {
                    @Override
                    public void onItemSelected(int currentPosition) {
                        tv_goods_type.setText(list_goods_type.get(currentPosition));
                    }
                });

                break;
            case R.id.tv_vehicle_type:
                ItemChooseUtil.showItemWheel(this, list_vehicle_type, "Type", 0, new I_itemSelectedListener() {
                    @Override
                    public void onItemSelected(int currentPosition) {
                        tv_vehicle_type.setText(list_vehicle_type.get(currentPosition));
                    }
                });
                break;
            case R.id.tv_ok:
                save();
                break;
        }
    }

}
