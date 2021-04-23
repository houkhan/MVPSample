package com.android.mvpsample.ui.contract;

import com.android.mvpsample.bean.TestBean;

import base.BaseModel;
import base.BasePresenter;
import base.BaseView;
import rx.Observable;

/**
 * @Author :韩帅帅
 * @Date :2021/4/23 - 2:24 PM
 * @Description :
 */
public interface HomeContract {
    interface Model extends BaseModel {
        Observable<TestBean> getTest();
    }

    interface View extends BaseView {
        void returnTestData(TestBean data);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getTest();
    }
}
