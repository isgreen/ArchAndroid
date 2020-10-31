package br.com.isgreen.archandroid.helper.resource

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.data.model.theme.Theme
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

class ResourceHelperImpl : ResourceHelper {

    companion object {
        val themes = listOf(
            Theme(R.drawable.ic_light, R.string.light, AppCompatDelegate.MODE_NIGHT_NO),
            Theme(R.drawable.ic_dark, R.string.dark, AppCompatDelegate.MODE_NIGHT_YES),
            Theme(R.drawable.ic_smartphone, R.string.follow_system, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        )
    }

    override suspend fun fetchThemes(): List<Theme> {
        return suspendCancellableCoroutine { continuation ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                continuation.resume(themes)
            } else {
                continuation.resume(themes.filterNot { it.mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM })
            }
        }
    }

    override suspend fun fetchMergeStrategies(): List<MergeStrategy> {
        return suspendCancellableCoroutine { continuation ->
            val mergeStrategies = listOf(
                MergeStrategy(R.string.merge_commit, "merge_commit"),
                MergeStrategy(R.string.squash, "squash"),
                MergeStrategy(R.string.fast_forward, "fast_forward")
            )
            continuation.resume(mergeStrategies)
        }
    }
}