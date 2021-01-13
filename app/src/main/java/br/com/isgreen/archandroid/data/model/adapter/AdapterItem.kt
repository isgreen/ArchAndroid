package br.com.isgreen.archandroid.data.model.adapter

import br.com.isgreen.archandroid.base.BaseAdapter

/**
 * Created by Éverdes Soares on 01/12/2021.
 */

open class AdapterItem(
    @Transient val viewType: Int = BaseAdapter.VIEW_TYPE_ITEM
)