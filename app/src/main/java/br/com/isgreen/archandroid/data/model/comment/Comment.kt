package br.com.isgreen.archandroid.data.model.comment

import android.os.Parcelable
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

@Parcelize
data class Comment(

    @SerializedName("id")
    val id: Int?,
    @SerializedName("content")
    val content: Content?,
    @SerializedName("created_on")
    val createdOn: String?,
    @SerializedName("deleted")
    val deleted: Boolean?,
    @SerializedName("pullrequest")
    val pullRequest: PullRequest?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updated_on")
    val updatedOn: String?,
    @SerializedName("user")
    val user: User?
): Parcelable


