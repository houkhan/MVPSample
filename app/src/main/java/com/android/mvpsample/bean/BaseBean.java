package com.android.mvpsample.bean;
/**
 * @Author :韩帅帅
 * @date : 2021/3/16 - 2:30 PM
 * @Description : BaseBean
 */
public class BaseBean {

    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
