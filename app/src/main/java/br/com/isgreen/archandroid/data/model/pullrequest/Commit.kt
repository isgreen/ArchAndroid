package br.com.isgreen.archandroid.data.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

data class Commit(
    @SerializedName("hash")
    val hash: String?,
    @SerializedName("type")
    val type: String?
)