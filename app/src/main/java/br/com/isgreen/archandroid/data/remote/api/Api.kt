package br.com.isgreen.archandroid.data.remote.api

import br.com.isgreen.archandroid.BuildConfig
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.repository.FetchRepositoriesResponse
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * Created by Éverdes Soares on 08/04/2019.
 */

interface Api {

    class Factory {

        companion object {
            fun create(preferencesHelper: PreferencesHelper): Api {
                val okHttpClient: OkHttpClient.Builder =
                    OkHttpClient.Builder()
//                        .addNetworkInterceptor(StethoInterceptor())
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                val requestInterceptor = RequestInterceptor(preferencesHelper)
                okHttpClient.addInterceptor(interceptor)
                    .addInterceptor(requestInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(okHttpClient.build())
                    .build()

                return retrofit.create(Api::class.java)
            }
        }
    }

    @Headers(
        "client_id: " + BuildConfig.CLIENT_ID,
        "Authorization: Basic " + BuildConfig.AUTHORIZATION_BASIC
    )
    @FormUrlEncoded
    @POST(ApiConstant.DO_LOGIN)
    suspend fun doLogin(
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Authorization

    @Headers(
        "client_id: " + BuildConfig.CLIENT_ID,
        "Authorization: Basic " + BuildConfig.AUTHORIZATION_BASIC
    )
    @FormUrlEncoded
    @POST(ApiConstant.DO_LOGIN)
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Authorization

    @GET(ApiConstant.FETCH_USER)
    suspend fun fetchUser(): User

    @GET(ApiConstant.FETCH_REPOSITORIES)
    suspend fun fetchRepos(
        @Query("sort") sort: String?,
        @Query("role") role: String?,
        @Query("after") after: String?
    ): FetchRepositoriesResponse

}