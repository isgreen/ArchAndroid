package br.com.isgreen.archandroid.data.model.pullrequest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

@Parcelize
data class Branch(
    @SerializedName("name")
    val name: String?
) : Parcelable