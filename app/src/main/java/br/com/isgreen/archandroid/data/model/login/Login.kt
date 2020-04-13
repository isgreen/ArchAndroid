package br.com.isgreen.archandroid.data.model.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Éverdes Soares on 08/23/2019.
 */

@Parcelize
data class Login(
    val username: String,
    val password: String
) : Parcelable