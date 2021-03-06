package br.com.isgreen.archandroid.data.model.pullrequest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Éverdes Soares on 11/20/2020.
 */

@Parcelize
@Deprecated("Unused - 01/01/2021")
data class PullRequestDeclineResponse(

    @SerializedName("id")
    val id: Int?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("closed_on")
    val closedOn: String?,
    @SerializedName("updated_on")
    val updatedOn: String?,

) : Parcelable
