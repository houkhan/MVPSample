package com.android.mvpsample.api;


import android.text.TextUtils;
import android.util.SparseArray;

import com.android.mvpsample.BuildConfig;
import com.android.mvpsample.app.AppApplication;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 120000;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 120000;
    public Retrofit retrofit;
    public ApiService movieService, qiNiuService, eSService;
    public OkHttpClient okHttpClient;
    private static SparseArray<Api> sRetrofitManager = new SparseArray<>(ApiConstants.TYPE_COUNT);

    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    private Api(int hostType) {

        //缓存
        File cacheFile = new File(AppApplication.getAppContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        //增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .build();
                return chain.proceed(build);
            }
        };

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(new HttpLogInterceptor())
                .cache(cache)
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        String baseUrl = "";
        switch (hostType) {
            case 0:
                baseUrl = ApiConstants.getHost(BuildConfig.HOST_TYPE);
                break;
        }
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        switch (hostType) {
            case 0:
                movieService = retrofit.create(ApiService.class);
                break;
        }
    }

    public static ApiService getDefault() {
        Api retrofitManager = sRetrofitManager.get(BuildConfig.HOST_TYPE);
        if (retrofitManager == null) {
            retrofitManager = new Api(ApiConstants.DEFAULT);
            sRetrofitManager.put(BuildConfig.HOST_TYPE, retrofitManager);
        }
        return retrofitManager.movieService;
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    /**
     * 不验证证书的OkHttpClient
     *
     * @param mRewriteCacheControlInterceptor
     * @param headerInterceptor
     * @param logInterceptor
     * @param cache
     * @return
     */
    private static OkHttpClient getUnsafeOkHttpClient(Interceptor mRewriteCacheControlInterceptor, Interceptor headerInterceptor, Interceptor logInterceptor, Cache cache) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
            builder.addInterceptor(mRewriteCacheControlInterceptor);
            builder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
            builder.addInterceptor(headerInterceptor);
            builder.addInterceptor(logInterceptor);
            builder.cache(cache);
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 配置自定义证书OkHttpClient
     *
     * @param mRewriteCacheControlInterceptor
     * @param headerInterceptor
     * @param logInterceptor
     * @param cache
     * @return
     */
    private static OkHttpClient getSafeOkHttpClient(Interceptor mRewriteCacheControlInterceptor, Interceptor headerInterceptor, Interceptor logInterceptor, Cache cache) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream certificate = AppApplication.getAppContext().getAssets().open("gdroot-g2.crt");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            String certificateAlias = Integer.toString(0);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
            SSLContext sslContext = SSLContext.getInstance("TLS");
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
            builder.addInterceptor(mRewriteCacheControlInterceptor);
            builder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
            builder.addInterceptor(headerInterceptor);
            builder.addInterceptor(logInterceptor);
            builder.cache(cache);
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}