package com.app.demo.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.demo.AppManager;
import com.app.demo.MyApplication;
import com.app.demo.R;
import com.app.demo.beans.OrderBean;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.beans.EventMessage;
import com.app.shop.mylibrary.utils.GlideEngine;
import com.app.shop.mylibrary.utils.GlideUtils;
import com.app.shop.mylibrary.utils.StringUtil;
import com.app.shop.mylibrary.utils.TimeUtil;
import com.app.shop.mylibrary.utils.ToastUtil;
import com.app.shop.mylibrary.utils.UserManager;
import com.app.shop.mylibrary.widgts.LoadingDialog;
import com.app.shop.mylibrary.widgts.timepicker.TimePickerCallback;
import com.app.shop.mylibrary.widgts.timepicker.TimePickerUtil;
import com.app.shop.mylibrary.widgts.timepicker.TimePickerView;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddOrderActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.imgv)
    ImageView imgv;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_location)
    EditText edtLocation;
    @BindView(R.id.tv_time)
    TextView tv_time;

    String path;

    private Dialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        ButterKnife.bind(this);
        // Dynamic permission for camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        if (savedInstanceState != null) {
            // Recycled
        } else {
            clearCache();
        }
        mLoadingDialog = LoadingDialog.createLoadingDialog(this, true, "Uploading image, please wait...");


        tvTitle.setText("New delivery");
        Places.initialize(getApplicationContext(), "AIzaSyB5wUyaAeOAmX54KacwLm7810UErPsp6lA");
//        edtLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    int AUTOCOMPLETE_REQUEST_CODE = 1;
//                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
//                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
//                            .build(AddOrderActivity.this);
//                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//                } else {
//
//                }
//            }
//        });
        edtLocation.setOnClickListener(view -> {
            int AUTOCOMPLETE_REQUEST_CODE = 1;
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(AddOrderActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    // Event callback method
    @Override
    public void onEvent(EventMessage msg) {
        super.onEvent(msg);
        if (msg.getMessageType() == EventMessage.refresh) {
            finish();
        }
    }

    // Method to save order
    private void save() {
        // Getting data from EditTexts
        String name = edtName.getText().toString();
        String time = tv_time.getText().toString();
        String location = edtLocation.getText().toString();

        // Check if any field is empty
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(time) || StringUtil.isEmpty(location)) {
            ToastUtil.showToast(this, "Order details cannot be empty");
            return;
        }

        // Creating new order
        OrderBean orderBean = new OrderBean(0,"","","","","","","","","","","","","","","");
        orderBean.setO_id(System.currentTimeMillis() + "");
        orderBean.setImg(path);
        orderBean.setTime(time);
        orderBean.setUser_to(name);
        orderBean.setLocation(location);
        orderBean.setUser_id(UserManager.getUserId(this));
        orderBean.setUser_name(UserManager.getUserName(this));

//        orderBean.o_id = System.currentTimeMillis() + "";
//        orderBean.img = path;
//        orderBean.time = time;
//        orderBean.user_to = name;
//        orderBean.location = location;
//        orderBean.user_id = UserManager.getUserId(this);
//        orderBean.user_name = UserManager.getUserName(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", orderBean);
        showActivity(AddOrderActivity.this, AddOrder2Activity.class, bundle);

    }

    // Click event handlers for different views in the layout
    @OnClick({R.id.imgv_return, R.id.tv_time, R.id.imgv, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_return:
                onBackPressed();
                break;
            case R.id.tv_time:
                TimePickerUtil timePickerUtil = new TimePickerUtil();
                timePickerUtil.setDatePicker(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN, null, null, null, new TimePickerCallback() {
                    @Override
                    public void setTimeCallback(int order, int year, int month, int day, int hour, int min, String idcard) {
                        String date = TimeUtil.getTodayData("yyyy") + "-" + StringUtil.onToTwo(month) + "-" + StringUtil.onToTwo(day) + " " + StringUtil.onToTwo(hour) + ":" + StringUtil.onToTwo(min) + ":00";
                        tv_time.setText(date);
                    }
                });
                timePickerUtil.showTimePicker(0);
                break;

            case R.id.imgv:
                showPicSeleDialog();
                break;
            case R.id.tv_ok:
                save();
                break;
        }
    }


    // Method to show Picture Selector dialog for choosing picture
    protected void showPicSeleDialog() {
        // Entering the album
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// All.PictureMimeType.ofAll(), picture.ofImage(), video.ofVideo(), audio.ofAudio()
                .theme(R.style.picture_default_style)// Set theme style
                .isWeChatStyle(true)// Simulate WeChat panel
                .maxSelectNum(1)// Maximum number of pictures to select
                .isGif(true) // Show gifs
                .queryMaxFileSize(20)  // Maximum picture size  20Mb
                .isEnableCrop(true)// Enable cropping
                .imageEngine(GlideEngine.createGlideEngine())// Pass in external image loading engine, required
                .selectionMode(PictureConfig.MULTIPLE)// Multiple or single selection
                .isCamera(true)// Show camera button
                .isZoomAnim(true)// Picture list click zoom effect
                .imageFormat(PictureMimeType.PNG_Q)// Save picture format suffix after taking a picture, default is jpeg
                .synOrAsy(true)// Sync
                .compressSavePath(getCompressPath())// Custom saving address
                .isCompress(true)
                //.sizeMultiplier(0.5f)// glide load image size 0~1 between, if set .glideOverride() is invalid
                .glideOverride(160, 160)// glide load width and height
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    // Method to clear cache including cropping, compression, AndroidQToPath generated files
    private void clearCache() {
        // Clear picture cache, including cropped and compressed pictures
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(this);
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    // Method to get path where the compressed image file will be stored
    private String getCompressPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PictureSelector/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    String file_path;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            edtLocation.clearFocus();
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                edtLocation.setText(place.getName());
            }
        }else{
            if (resultCode == RESULT_OK) { // If the return code is usable
                switch (requestCode) {
                    case PictureConfig.CHOOSE_REQUEST:
                        List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
                        for (int i = 0; i < mediaList.size(); i++) {
                            if (mediaList.get(i) != null) { // Compression
                                LocalMedia media = mediaList.get(i);
                                if (media.isCompressed()) {
                                    file_path = media.getCompressPath();
                                }
                                if (StringUtil.isEmpty(file_path)) {
                                    file_path = media.getCutPath();
                                }
                                if (StringUtil.isEmpty(file_path)) {
                                    file_path = media.getPath();
                                }
                                if (StringUtil.isEmpty(file_path)) {
                                    file_path = "defalut";
                                }


                                initUploadHttp(new File(file_path));

                            }
                            LoadingDialog.showDialog(mLoadingDialog);

                        }
                        // Upload complete

                        LoadingDialog.closeDialog(mLoadingDialog);


                        break;
                }
            }
        }
    }

    // Base URLs for setting and getting pictures
    String BASE_URL_SET_PIC = "http://120.79.198.127:8080/hello/setImage?picbeanstr=";
    String BASE_URL_GET_PIC = "http://120.79.198.127:8080/hello/getImage?name=";

    // Method to initialize HTTP upload
    private void initUploadHttp(File file) {
        final String name = System.currentTimeMillis() + "";
        // Splice parameters
        final PostRequest request = OkGo.post(BASE_URL_SET_PIC + name).tag(this);
        request.params("uploadFile", file);

        Log.v(MyApplication.TAG, "name :" + name.toString());
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.v(MyApplication.TAG + "pic:", response.toString());
                Log.v(MyApplication.TAG + "pic url:", BASE_URL_GET_PIC + name);
                path = BASE_URL_GET_PIC + name;
                GlideUtils.getInstance().loadImage(AddOrderActivity.this, path, imgv);
            }
        });
    }
}
