package br.com.isgreen.archandroid.data.model.comment

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

data class Content(
    @SerializedName("html")
    val html: String?,
    @SerializedName("markup")
    val markup: String?,
    @SerializedName("raw")
    val raw: String?,
    @SerializedName("type")
    val type: String?
)