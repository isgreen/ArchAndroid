package br.com.isgreen.archandroid.data.model.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("uuid")
    val uuid: String?
) : Parcelable