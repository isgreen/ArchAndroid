package br.com.isgreen.archandroid.data.model.pullrequest

import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 12/29/2020.
 */

enum class PullRequestState(val value: String) {

    @SerializedName("OPEN")
    OPEN("OPEN"),
    @SerializedName("MERGED")
    MERGED("MERGED"),
    @SerializedName("DECLINED")
    DECLINED("DECLINED"),
    @SerializedName("SUPERSEDED")
    SUPERSEDED("SUPERSEDED")

}