package br.com.isgreen.archandroid.data.model.commit

import com.google.gson.annotations.SerializedName


/**
 * Created by Éverdes Soares on 08/12/2020.
 */

data class FetchPullRequestCommitsResponse(

    @SerializedName("page")
    val page: Int,
    @SerializedName("pagelen")
    val pagelen: Int,
    @SerializedName("values")
    val commits: List<Commit>

)
