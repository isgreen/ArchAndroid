package br.com.isgreen.archandroid.data.model.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Href(
    @SerializedName("href")
    val href: String?
) : Parcelable