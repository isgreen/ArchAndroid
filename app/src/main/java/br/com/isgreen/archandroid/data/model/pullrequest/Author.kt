package br.com.isgreen.archandroid.data.model.pullrequest

import android.os.Parcelable
import br.com.isgreen.archandroid.data.model.login.User
import br.com.isgreen.archandroid.data.model.repository.Link
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

@Parcelize
data class Author(
    @SerializedName("account_id")
    val accountId: String?,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("user")
    val user: User?
) : Parcelable