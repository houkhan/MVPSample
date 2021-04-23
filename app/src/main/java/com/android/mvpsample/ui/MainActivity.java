package com.android.mvpsample.ui;

import android.widget.TextView;

import com.android.mvpsample.R;
import com.android.mvpsample.bean.TestBean;
import com.android.mvpsample.ui.contract.HomeContract;
import com.android.mvpsample.ui.model.HomeModel;
import com.android.mvpsample.ui.presenter.HomePresenter;

import base.BaseActivity;

public class MainActivity extends BaseActivity<HomePresenter, HomeModel> {

    private TextView tv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(new HomeContract.View() {
            @Override
            public void returnTestData(TestBean data) {
                tv.setText(data.toString());
            }

            @Override
            public void showLoading(String title) {

            }

            @Override
            public void stopLoading() {

            }

            @Override
            public void showErrorTip(String msg) {

            }
        },mModel);
    }

    @Override
    public void initView() {
        tv = findViewById(R.id.tv);

    }

    @Override
    public void initData() {
        mPresenter.getTest();
    }
}