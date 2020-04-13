package org.universal.bibliafiel_comentada.helper.exception

import androidx.annotation.StringRes
import br.com.isgreen.archandroid.R
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

class ExceptionHelper {

    companion object {
        const val NO_CODE = -1

        fun getMessage(exception: Throwable): Any {
            return getMessage(
                exception,
                true,
                R.string.there_was_fail_try_again
            )
        }

        fun getMessage(exception: Throwable, readApiMessage: Boolean): Any {
            return getMessage(
                exception,
                readApiMessage,
                R.string.there_was_fail_try_again
            )
        }

        fun getMessage(
            exception: Throwable,
            readApiMessage: Boolean,
            @StringRes defaultMessageRes: Int
        ): Any {
            val message: Any? = when (exception) {
                is HttpException -> getErrorMessage(
                    exception,
                    readApiMessage
                )
                is UnknownHostException ->
                    R.string.no_internet_signal
                is SocketTimeoutException ->
                    R.string.no_server_response
                else ->
                    defaultMessageRes
            }

            message?.let {
                return it
            }

            return defaultMessageRes
        }

        fun getCode(exception: Throwable): Int {
            return if (exception is HttpException) {
                exception.code()
            } else {
                NO_CODE
            }
        }

        private fun getErrorMessage(exception: HttpException, readApiMessage: Boolean): Any? {
            if (readApiMessage) {
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
                                    error.getJSONObject("validationErros")
                                        .getString("email")
                                } else {
                                    error.getString("validationErros")
                                }
                            }

                            else -> error.getString("message")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (exception.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                return R.string.no_data
            } else if (exception.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                return R.string.failed_server
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

}