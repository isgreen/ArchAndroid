package br.com.isgreen.archandroid.data.remote.api

import br.com.isgreen.archandroid.BuildConfig
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.repository.FetchReposResponse
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

    @FormUrlEncoded
    @POST(ApiConstant.DO_LOGIN)
    suspend fun doLogin(
        @Header("client_id") clientId: String,
        @Header("Authorization") basicToken: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Authorization

    @FormUrlEncoded
    @POST(ApiConstant.DO_LOGIN)
    suspend fun refreshToken(
        @Header("client_id") clientId: String,
        @Header("Authorization") basicToken: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Authorization

    @GET(ApiConstant.FETCH_USER)
    suspend fun fetchUser(): User

    @GET(ApiConstant.FETCH_USER_REPOS)
    suspend fun fetchUserRepos(
        @Path("userUuid") userUuid: String
    ): FetchReposResponse

    @GET(ApiConstant.FETCH_REPOSITORIES)
    suspend fun fetchRepos(
        @Query("sort") sort: String?,
        @Query("role") role: String?,
        @Query("after") after: String?
    ): FetchReposResponse

    @GET(ApiConstant.FETCH_PULL_REQUESTS)
    suspend fun fetchPullRequests(
        @Path("userUuid") userUuid: String
    ): FetchPullRequestsResponse

    @GET(ApiConstant.FETCH_PULL_REQUEST_COMMITS)
    suspend fun fetchPullRequestCommits(
        @Path("pullRequestId") pullRequestId: Int,
        @Path("repoFullName") repoFullName: String
    ): FetchPullRequestCommitsResponse

}