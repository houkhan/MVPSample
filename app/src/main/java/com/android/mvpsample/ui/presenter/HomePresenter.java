package com.android.mvpsample.ui.presenter;

import com.android.mvpsample.api.RxSubscriber;
import com.android.mvpsample.bean.TestBean;
import com.android.mvpsample.ui.contract.HomeContract;

/**
 * @Author :韩帅帅
 * @Date :2021/4/23 - 2:24 PM
 * @Description :
 */
public class HomePresenter extends HomeContract.Presenter {
    @Override
    public void getTest() {
        mRxManage.add(mModel.getTest().subscribe(new RxSubscriber<TestBean>(mContext, true) {
            @Override
            protected void _onNext(TestBean data) {
                mView.returnTestData(data);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
