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

import com.app.demo.MyApplication;
import com.app.demo.R;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.beans.UserBean;
import com.app.shop.mylibrary.utils.GlideEngine;
import com.app.shop.mylibrary.utils.GlideUtils;
import com.app.shop.mylibrary.utils.StringUtil;
import com.app.shop.mylibrary.utils.ToastUtil;
import com.app.shop.mylibrary.utils.UserManager;
import com.app.shop.mylibrary.widgts.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPwd;
    @BindView(R.id.edt_password_confirm)
    EditText edtPwdConfirm;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.imgv)
    ImageView imgv;

    String path;

    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        tvTitle.setText("Sign Up");
        // Enable dynamic camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        if (savedInstanceState != null) {
            // The instance was recycled
        } else {
            clearCache();
        }
        mLoadingDialog = LoadingDialog.createLoadingDialog(this, true, "Uploading picture, please wait...");

    }
    // Method for handling click events on the views
    @OnClick({R.id.imgv_return, R.id.imgv, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_return:
                onBackPressed();
                break;
            case R.id.imgv:
                showPicSeleDialog();
                break;
            case R.id.tv_register:

                String name = edtName.getText().toString();
                String user_id = edtUserName.getText().toString();
                String pwd = edtPwd.getText().toString();
                String pwd2 = edtPwdConfirm.getText().toString();
                String phone = edtPhone.getText().toString();

                // Check if the user inputs are complete
                if (StringUtil.isEmpty(name) || StringUtil.isEmpty(user_id) || StringUtil.isEmpty(pwd) || StringUtil.isEmpty(pwd2) || StringUtil.isEmpty(phone)) {
                    ToastUtil.showToast(this, "Please complete the user information");
                    return;
                }

                // Check if the two passwords are the same
                if (!pwd.equals(pwd2)) {
                    ToastUtil.showToast(this, "The two passwords are not the same");
                    return;
                }

                // Check if the user already exist
                if (UserManager.isExistedUser(user_id)) {
                    ToastUtil.showToast(this, "This user already exists");
                    return;
                }

                // Save the new user
                UserBean userBean = new UserBean();
                userBean.setUser_id(user_id);
                userBean.setName(name);
                userBean.setMobile(phone);
                userBean.setPassword(pwd);
                userBean.save();
                ToastUtil.showToast(this, "Registration Success");
                onBackPressed();
                break;
        }
    }

    // Method for opening the picture selection dialog
    protected void showPicSeleDialog() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// All.PictureMimeType.ofAll(), picture.ofImage(), video.ofVideo(), audio.ofAudio()
                .theme(R.style.picture_default_style)// Set theme style
                .isWeChatStyle(true)
                .maxSelectNum(1)
                .isGif(true) // Show gifs
                .queryMaxFileSize(20)  // Maximum picture size  20Mb
                .isEnableCrop(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)
                .isCamera(true)// camera button
                .isZoomAnim(true)// Picture list click zoom effect
                .imageFormat(PictureMimeType.PNG_Q)
                .synOrAsy(true)// sync
                .compressSavePath(getCompressPath())// custom saving address
                .isCompress(true)
                .glideOverride(160, 160)// width and height
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    // Method for clearing cache
    private void clearCache() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PictureFileUtils.deleteAllCacheDirFile(this);
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    // Method for getting the path of the compressed image
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

        if (resultCode == RESULT_OK) { // If the resultCode is usable
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < mediaList.size(); i++) {
                        if (mediaList.get(i) != null) { //compressed
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
                    // Upload completed
                    LoadingDialog.closeDialog(mLoadingDialog);
                    break;
            }
        }
    }

    // Base URLs for setting and getting pictures
    String BASE_URL_SET_PIC = "http://120.79.198.127:8080/hello/setImage?picbeanstr=";
    String BASE_URL_GET_PIC = "http://120.79.198.127:8080/hello/getImage?name=";

    // Method to initialize HTTP upload
    private void initUploadHttp(File file) {
        final String name = System.currentTimeMillis() + "";
        // splice parameters
        final PostRequest request = OkGo.post(BASE_URL_SET_PIC + name).tag(this);
        request.params("uploadFile", file);

        Log.v(MyApplication.TAG, "name :" + name.toString());
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.v(MyApplication.TAG + "pic:", response.toString());
                Log.v(MyApplication.TAG + "pic url:", BASE_URL_GET_PIC + name);
                path = BASE_URL_GET_PIC + name;
                GlideUtils.getInstance().loadImage(RegisterActivity.this, path, imgv);
            }
        });
    }
}
