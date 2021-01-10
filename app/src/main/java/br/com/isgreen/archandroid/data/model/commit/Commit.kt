package br.com.isgreen.archandroid.data.model.commit
import android.os.Parcelable
import br.com.isgreen.archandroid.data.model.pullrequest.Author
import br.com.isgreen.archandroid.data.model.pullrequest.Summary
import br.com.isgreen.archandroid.data.model.repository.Repo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * Created by Éverdes Soares on 08/12/2020.
 */

@Parcelize
data class Commit(
    @SerializedName("author")
    val author: Author?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("hash")
    val hash: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("repository")
    val repository: Repo?,
    @SerializedName("summary")
    val summary: Summary?,
    @SerializedName("type")
    val type: String?
) : Parcelable

