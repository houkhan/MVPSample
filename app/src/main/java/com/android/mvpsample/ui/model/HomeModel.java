package com.android.mvpsample.ui.model;

import com.android.mvpsample.api.Api;
import com.android.mvpsample.bean.TestBean;
import com.android.mvpsample.ui.contract.HomeContract;

import baserx.RxSchedulers;
import rx.Observable;

/**
 * @Author :韩帅帅
 * @Date :2021/4/23 - 2:24 PM
 * @Description :
 */
public class HomeModel implements HomeContract.Model {
    @Override
    public Observable<TestBean> getTest() {
        return Api.getDefault().getTestData()
                .compose(RxSchedulers.<TestBean>io_main());
    }
}
