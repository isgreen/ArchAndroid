package br.com.isgreen.archandroid.data.model.comment

import com.google.gson.annotations.SerializedName


/**
 * Created by Éverdes Soares on 08/17/2020.
 */

data class FetchPullRequestCommentsResponse(

    @SerializedName("page")
    val page: Int,
    @SerializedName("pagelen")
    val pagelen: Int,
    @SerializedName("values")
    val comments: List<Comment>,
    @SerializedName("next")
    val next: String?

)
