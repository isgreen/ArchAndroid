package br.com.isgreen.archandroid.data.model.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainBranch(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?
) : Parcelable