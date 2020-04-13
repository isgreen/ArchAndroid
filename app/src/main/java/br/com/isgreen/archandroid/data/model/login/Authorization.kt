package br.com.isgreen.archandroid.data.model.login
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by Éverdes Soares on 03/29/2020.
 */

@Parcelize
data class Authorization(

    @SerializedName("scopes")
    val scopes: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("access_token")
    var accessToken: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?

) : Parcelable