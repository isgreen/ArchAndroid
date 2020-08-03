package br.com.isgreen.archandroid.data.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

data class Summary(
    @SerializedName("html")
    val html: String?,
    @SerializedName("markup")
    val markup: String?,
    @SerializedName("raw")
    val raw: String?,
    @SerializedName("type")
    val type: String?
)