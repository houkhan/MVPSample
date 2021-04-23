/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.android.mvpsample.api;


public class ApiConstants {
    /**
     * Host
     */
    public static final int TYPE_COUNT = 1;
    public static final int SUCCESS = 200;
    public static final int DEFAULT = 0;


    public static final String DEVELOP_HOST = "https://www.wanandroid.com";//0.开发环境
    public static final String TEXT_HOST = "";  // 1.测试环境
    public static final String QUASI_HOST = "";//2.预发布
    public static final String PRODUCTION_HOST = "";//3.生产环境
    /**
     * 全局常量
     */
    public static final String USERID = "userId";
    public static final String SEARCHSCHOSPITALBEAN = "SearchSCHospitalBean";
    public static final String SEARCHSCOUTPATIENTID = "searchSCOutpatientId";
    public static final String SEARCHSCNEWSTYPEBEAN = "SearchSCNewsTypeBean";
    public static final String SEARCHCOMMODITYBEAN = "SearchCommodityBean";
    public static final String PAYSUCCESS = "PaySuccess";



    public static String getHost(int id) {
        switch (id) {
            case 0:
                return DEVELOP_HOST;
            case 1:
                return TEXT_HOST;
            case 2:
                return QUASI_HOST;
            case 3:
                return PRODUCTION_HOST;
            default:
                break;
        }
        return DEVELOP_HOST;
    }

}
