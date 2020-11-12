package br.com.isgreen.archandroid.data.model.merge

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 11/11/2020.
 */

data class PullRequestMergeParameter(

    @SerializedName("type")
    val type: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("merge_strategy")
    val mergeStrategy: String,
    @SerializedName("close_source_branch")
    val closeSourceBranch: Boolean

)