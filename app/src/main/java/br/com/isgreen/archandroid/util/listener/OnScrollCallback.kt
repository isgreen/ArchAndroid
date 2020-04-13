package br.com.isgreen.archandroid.util.listener

/**
 * Created by Éverdes Soares on 10/21/2019
 */

abstract class OnScrollCallback {
    open fun onScrolled(visibleItem: Int) {}
    open fun onLoadMore(page: Int, totalItemsCount: Int) {}
    open fun onHide() {}
    open fun onShow() {}
}