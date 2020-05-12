package br.com.isgreen.archandroid.util.listener

/**
 * Created by Éverdes Soares on 10/21/2019
 */

abstract class OnScrollCallback {
    open fun onHide() {}
    open fun onShow() {}
    open fun onScrolledToLastItem() {}
    open fun onScrolledToFirstItem() {}
    open fun onScrolled(visibleItem: Int) {}
    open fun onScrollPage(page: Int, totalItemsCount: Int) {}
}