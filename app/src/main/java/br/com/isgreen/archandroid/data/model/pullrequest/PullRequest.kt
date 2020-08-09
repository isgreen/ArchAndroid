package br.com.isgreen.archandroid.data.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

data class PullRequest(

    @SerializedName("id")
    val id: Int?,
    @SerializedName("author")
    val author: Author?,
    @SerializedName("close_source_branch")
    val closeSourceBranch: Boolean?,
    @SerializedName("closed_by")
    val closedBy: Any?,
    @SerializedName("comment_count")
    val commentCount: Int?,
    @SerializedName("created_on")
    val createdOn: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("destination")
    val destination: Destination?,
    @SerializedName("merge_commit")
    val mergeCommit: Any?,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("source")
    val source: Source?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("summary")
    val summary: Summary?,
    @SerializedName("task_count")
    val taskCount: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updated_on")
    val updatedOn: String?

)