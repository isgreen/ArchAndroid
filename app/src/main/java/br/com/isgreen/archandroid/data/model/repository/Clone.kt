package br.com.isgreen.archandroid.data.model.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clone(
    @SerializedName("href")
    val href: String?,
    @SerializedName("name")
    val name: String?
) : Parcelable