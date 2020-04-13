package br.com.isgreen.archandroid.data.model.repository

import com.google.gson.annotations.SerializedName


/**
 * Created by Éverdes Soares on 04/02/2020.
 */

data class FetchRepositoriesResponse(
    @SerializedName("next")
    val next: String?,
    @SerializedName("pagelen")
    val pagelen: Int?,
    @SerializedName("values")
    val repos: List<Repo>?
)
