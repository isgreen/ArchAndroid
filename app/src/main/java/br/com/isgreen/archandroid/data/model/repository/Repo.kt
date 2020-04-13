package br.com.isgreen.archandroid.data.model.repository
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by Éverdes Soares on 09/22/2019.
 */

@Parcelize
data class Repo(

    @SerializedName("created_on")
    val createdOn: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("fork_policy")
    val forkPolicy: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("has_issues")
    val hasIssues: Boolean?,
    @SerializedName("has_wiki")
    val hasWiki: Boolean?,
    @SerializedName("is_private")
    val isPrivate: Boolean?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("mainbranch")
    val mainBranch: MainBranch?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("owner")
    val owner: Owner?,
    @SerializedName("project")
    val project: Project?,
    @SerializedName("scm")
    val scm: String?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updated_on")
    val updatedOn: String?,
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("website")
    val website: String?

) : Parcelable
