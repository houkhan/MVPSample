package com.android.mvpsample.api;


import com.android.mvpsample.bean.TestBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @Author :韩帅帅
 * @date : 2021/3/17 - 4:29 PM
 * @Description : 网络请求接口
 */
public interface ApiService {
    /**
     * 登录
     *
     * @return
     */
    @GET("/wxarticle/chapters/json")
    Observable<TestBean> getTestData();

}
