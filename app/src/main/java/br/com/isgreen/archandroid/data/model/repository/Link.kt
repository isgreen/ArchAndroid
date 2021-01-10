package br.com.isgreen.archandroid.data.model.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    @SerializedName("avatar")
    val avatar: Href?,
    @SerializedName("branches")
    val branches: Href?,
    @SerializedName("clone")
    val clone: List<Clone>?,
    @SerializedName("commits")
    val commits: Href?,
    @SerializedName("downloads")
    val downloads: Href?,
    @SerializedName("forks")
    val forks: Href?,
    @SerializedName("hooks")
    val hooks: Href?,
    @SerializedName("html")
    val html: Href?,
    @SerializedName("issues")
    val issues: Href?,
    @SerializedName("pullrequests")
    val pullRequests: Href?,
    @SerializedName("self")
    val self: Href?,
    @SerializedName("source")
    val source: Href?,
    @SerializedName("tags")
    val tags: Href?,
    @SerializedName("watchers")
    val watchers: Href?
) : Parcelable