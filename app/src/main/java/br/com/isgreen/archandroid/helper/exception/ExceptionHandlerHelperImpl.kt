package br.com.isgreen.archandroid.helper.exception

import android.content.Context
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.data.model.error.ErrorMessage
import org.json.JSONObject
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Éverdes Soares on 09/13/2019.
 */

class ExceptionHandlerHelperImpl(private val context: Context) : ExceptionHandlerHelper {

    override fun getErrorMessage(
        exception: Throwable,
        readApiMessage: Boolean?,
        defaultMessageRes: Int?
    ): ErrorMessage {
        val errorMessage = when (exception) {
            is HttpException ->
                getHttpErrorMessage(exception, readApiMessage)
            is UnknownHostException ->
                ErrorMessage(context.getString(R.string.no_internet_signal))
            is SocketTimeoutException ->
                ErrorMessage(context.getString(R.string.no_server_response))
            else ->
                null
        }

        errorMessage?.let {
            return it
        }

        val message = defaultMessageRes ?: R.string.there_was_fail_try_again
        return ErrorMessage(context.getString(message))
    }

    private fun getHttpErrorMessage(exception: HttpException, readApiMessage: Boolean?): ErrorMessage? {
        if (readApiMessage == true) {
            try {
                exception.response()?.errorBody()?.let {
                    val error = JSONObject(
                        convertStreamToString(
                            it.byteStream()
                        )
                    )
                    return when {
                        error.has("validationErros") -> {
                            if (error.getJSONObject("validationErros").has("email")) {
                                ErrorMessage(
                                    error.getJSONObject("validationErros").getString("email")
                                )
                            } else {
                                ErrorMessage(error.getString("validationErros"))
                            }
                        }

                        else -> ErrorMessage(
                            error.getJSONObject("error")
                                .getString("message")
                                .replace("newstatus: ", "")
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (exception.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            return ErrorMessage(context.getString(R.string.no_data), exception.code())
        } else if (exception.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            return ErrorMessage(context.getString(R.string.failed_server), exception.code())
        }


        return null
    }

    private fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        var stringBuilder = ""

        try {
            var line: String? = null
            while ({ line = reader.readLine(); line }() != null) {
                line?.let {
                    stringBuilder += line + "\n"
                }
            }
        } catch (error: OutOfMemoryError) {
            error.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return stringBuilder
    }
}