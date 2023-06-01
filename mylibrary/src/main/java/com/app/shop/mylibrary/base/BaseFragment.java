package com.app.shop.mylibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.shop.mylibrary.MyApplication;
import com.app.shop.mylibrary.R;
import com.app.shop.mylibrary.beans.EventMessage;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class BaseFragment extends Fragment {

    public BaseActivity mActivity;

    public BaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (BaseActivity) this.getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);

    }

    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.start_in, R.anim.start_out);
    }



    public void skipActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.start_in, R.anim.start_out);
    }


    public void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    public void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(EventMessage msg) {

    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onEventSticky(EventMessage msg) {

    }

}
