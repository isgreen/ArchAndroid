package br.com.isgreen.archandroid.helper.resource

import br.com.isgreen.archandroid.data.model.merge.MergeStrategy
import br.com.isgreen.archandroid.data.model.theme.Theme

/**
 * Created by Éverdes Soares on 01/14/2020.
 */

interface ResourceHelper {

    suspend fun fetchThemes(): List<Theme>

    suspend fun fetchMergeStrategies(): List<MergeStrategy>

}