package br.com.isgreen.archandroid.data.model.comment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

@Parcelize
data class Content(
    @SerializedName("html")
    val html: String?,
    @SerializedName("markup")
    val markup: String?,
    @SerializedName("raw")
    val raw: String?,
    @SerializedName("type")
    val type: String?
): Parcelable {
    constructor(
        raw: String?
    ) : this(
        raw = raw,
        html = null,
        markup = null,
        type = null
    )
}