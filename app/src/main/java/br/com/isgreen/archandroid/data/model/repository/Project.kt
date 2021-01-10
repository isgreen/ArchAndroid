package br.com.isgreen.archandroid.data.model.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    @SerializedName("key")
    val key: String?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uuid")
    val uuid: String?
) : Parcelable