package br.com.isgreen.archandroid.data.model.pullrequest

import br.com.isgreen.archandroid.data.model.repository.Repo
import com.google.gson.annotations.SerializedName

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

data class Source(
    @SerializedName("branch")
    val branch: Branch?,
    @SerializedName("commit")
    val commit: Commit?,
    @SerializedName("repository")
    val repository: Repo?
)