package com.android.mvpsample.api;

import android.content.Context;
import android.text.TextUtils;

import com.android.mvpsample.bean.BaseBean;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.net.SocketTimeoutException;

import baseapp.AppManager;
import baseapp.BaseApplication;
import baserx.ServerException;
import commonwidget.LoadingDialog;
import rx.Subscriber;

/**
 * des:订阅封装
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog = true;
    }

    public void hideDialog() {
        this.showDialog = false;
    }

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxSubscriber(Context context) {
        this(context, BaseApplication.getAppContext().getString(com.base.baselibrary.R.string.loading), true);
    }

    public RxSubscriber(Context context, boolean showDialog) {
        this(context, BaseApplication.getAppContext().getString(com.base.baselibrary.R.string.loading), showDialog);
    }

    @Override
    public void onCompleted() {
        if (showDialog)
            LoadingDialog.getLoadingDialog(mContext).cancelDialogForLoading();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showDialog) {
            try {
                LoadingDialog.getLoadingDialog(mContext).showDialogForLoading(msg,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onNext(T t) {
        if (showDialog)
            LoadingDialog.getLoadingDialog(mContext).cancelDialogForLoading();
        if (t instanceof BaseBean) {
            BaseBean data = (BaseBean) t;
            //此处进行请求接口错误处理  比如400 登录过期跳转登录
            if (400 == data.getErrorCode()) {
                ToastUtils.showShort(data.getErrorMsg());
                //LoginActivity.startAction(mContext);
                AppManager.getAppManager().finishAllActivity();
                return;
            }
        }
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        if (showDialog)
            LoadingDialog.getLoadingDialog(mContext).cancelDialogForLoading();
        e.printStackTrace();
        //网络
        if (!NetworkUtils.isConnected()) {
            _onError(BaseApplication.getAppContext().getString(com.base.baselibrary.R.string.no_net));
            LogUtils.e(e.getMessage());
            ToastUtils.showShort(BaseApplication.getAppContext().getString(com.base.baselibrary.R.string.net_error));
            NetWorkDisconnect();
        }
        //服务器
        else if (e instanceof ServerException) {
            _onError(e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage())) {
                ToastUtils.showLong(e.getMessage());
            }
        }
        //访问超时
        else if (e instanceof SocketTimeoutException) {
            _onError(e.getMessage());
            ToastUtils.showShort("请求超时");
        } else {
            //其他
            _onError(BaseApplication.getAppContext().getString(com.base.baselibrary.R.string.net_error));
            ToastUtils.showShort("系统异常");
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

    protected void NetWorkDisconnect() {

    }
}
