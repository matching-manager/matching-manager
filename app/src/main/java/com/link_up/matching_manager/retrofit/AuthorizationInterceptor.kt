package com.link_up.matching_manager.retrofit

import com.link_up.matching_manager.R
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    // API 통신을 할 떄 마 catch를 해서 특정 행위를 하고싶을 때 Interceptor 사용
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "KakaoAK %s".format(
//                    R.string.kakao_api_key
                    "1b17dcbddee95856256ea59c9bdfa8ad"
                )
            ).build()
        return chain.proceed(newRequest)
    }
}