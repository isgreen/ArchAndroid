package br.com.isgreen.archandroid.data.model.pullrequest

import android.os.Parcelable
import br.com.isgreen.archandroid.data.model.commit.Commit
import br.com.isgreen.archandroid.data.model.repository.Repo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Éverdes Soares on 08/02/2020.
 */

@Parcelize
data class Source(
    @SerializedName("branch")
    val branch: Branch?,
    @SerializedName("commit")
    val commit: Commit?,
    @SerializedName("repository")
    val repository: Repo?
) : Parcelable