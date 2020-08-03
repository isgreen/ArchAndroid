package br.com.isgreen.archandroid.data.model.pullrequest

import com.google.gson.annotations.SerializedName


/**
 * Created by Éverdes Soares on 04/02/2020.
 */

data class FetchPullRequestsResponse(

    @SerializedName("pagelen")
    val pagelen: Int,
    @SerializedName("values")
    val pullRequests: List<PullRequest>

)
