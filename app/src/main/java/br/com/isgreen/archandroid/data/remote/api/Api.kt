package br.com.isgreen.archandroid.data.remote.api

import br.com.isgreen.archandroid.BuildConfig
import br.com.isgreen.archandroid.data.local.PreferencesHelper
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.data.model.comment.FetchPullRequestCommentsResponse
import br.com.isgreen.archandroid.data.model.commit.FetchPullRequestCommitsResponse
import br.com.isgreen.archandroid.data.model.login.Authorization
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.merge.PullRequestMergeParameter
import br.com.isgreen.archandroid.data.model.pullrequest.FetchPullRequestsResponse
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequestMessage
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
    suspend fun refreshToken(
        @Header("client_id") clientId: String,
        @Header("Authorization") basicToken: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Authorization

    //region Login
    @FormUrlEncoded
    @POST(ApiConstant.DO_LOGIN)
    suspend fun doLogin(
        @Header("client_id") clientId: String,
        @Header("Authorization") basicToken: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Authorization
    //endregion Login

    //region User
    @GET(ApiConstant.FETCH_USER)
    suspend fun fetchUser(): User
    //endregion User

    @GET(ApiConstant.FETCH_USER_REPOS)
    suspend fun fetchUserRepos(
        @Path("user_uuid") userUuid: String
    ): FetchReposResponse

    //region Repositories
    @GET
    suspend fun fetchRepos(@Url url: String): FetchReposResponse
    //endregion Repositories

    //region Pull Request
    @GET
    suspend fun fetchPullRequests(@Url url: String): FetchPullRequestsResponse

    @GET
    suspend fun fetchPullRequestCommits(@Url url: String): FetchPullRequestCommitsResponse

    @GET
    suspend fun fetchPullRequestComments(@Url url: String): FetchPullRequestCommentsResponse

    @FormUrlEncoded
    @POST(ApiConstant.DO_PULL_REQUEST_MERGE)
    suspend fun doPullRequestsMerge(
        @Path("pullRequestId") pullRequestId: Int,
        @Path("repoFullName") repoFullName: String,
        @Field("pullrequest_merge_parameters") pullRequestMergeParameter: PullRequestMergeParameter
    )

    @POST(ApiConstant.DO_PULL_REQUEST_DECLINE)
    suspend fun doPullRequestDecline(
        @Path("workspace") workspace: String,
        @Path("repo_slug") repoSlug: String,
        @Path("pull_request_id") pullRequestId: Int
    ): PullRequest

    @POST(ApiConstant.SEND_PULL_REQUEST_COMMENT)
    suspend fun sendPullRequestComment(
        @Body pullRequestMessage: PullRequestMessage,
        @Path("workspace") workspace: String,
        @Path("repo_slug") repoSlug: String,
        @Path("pull_request_id") pullRequestId: Int
    ): Comment
    //endregion Pull Request
}