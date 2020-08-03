package br.com.isgreen.archandroid.data.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

data class Branch(
    @SerializedName("name")
    val name: String?
)