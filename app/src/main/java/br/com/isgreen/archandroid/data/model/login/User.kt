package br.com.isgreen.archandroid.data.model.login
import android.os.Parcelable
import br.com.isgreen.archandroid.data.model.repository.Link
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by Éverdes Soares on 08/23/2019.
 */

@Parcelize
data class User(

    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("account_status")
    val accountStatus: String,
    @SerializedName("created_on")
    val createdOn: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("is_staff")
    val isStaff: Boolean,
    @SerializedName("links")
    val links: Link,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("uuid")
    val uuid: String

) : Parcelable