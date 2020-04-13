package br.com.isgreen.archandroid.util.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Éverdes Soares on 10/21/2019
 */

class EndlessScrollListener : RecyclerView.OnScrollListener {

    companion object {
        const val LOADING_ITEM = 1
        const val HIDE_THRESHOLD = 20
        const val FIRST_PAGE_INDEX = 1
    }

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 5
    private var currentPage = 1
    private var scrolledDistance = 0
    private var previousTotalItemCount = 0

    private var loading = true
    private var mReverse = false
    private var controlsVisible: Boolean = false

    private var mOnScrollCallback: OnScrollCallback? = null

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    constructor(onScrollCallback: OnScrollCallback, layoutManager: LinearLayoutManager)
            : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
    }

    constructor(onScrollCallback: OnScrollCallback, layoutManager: LinearLayoutManager, reverse: Boolean)
            : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
        mReverse = reverse
    }

    constructor(onScrollCallback: OnScrollCallback, layoutManager: GridLayoutManager, reverse: Boolean)
            : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
        mReverse = reverse
    }

    constructor(
        onScrollCallback: OnScrollCallback, layoutManager: StaggeredGridLayoutManager,
        reverse: Boolean
    ) : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
        mReverse = reverse
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun getFirstVisibleItem(firstVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in firstVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = firstVisibleItemPositions[i]
            } else if (firstVisibleItemPositions[i] > maxSize) {
                maxSize = firstVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        mLayoutManager?.let { layoutManager ->
            var lastVisibleItemPosition = 0
            var firstVisibleItemPosition = 0
            val totalItemCount = layoutManager.itemCount

            when (layoutManager) {
                is StaggeredGridLayoutManager -> {
                    val lastVisibleItemPositions =
                        layoutManager.findLastVisibleItemPositions(null)
                    val firstVisibleItemPositions =
                        layoutManager.findFirstVisibleItemPositions(null)
                    // get maximum element within the list
                    lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
                    firstVisibleItemPosition = getFirstVisibleItem(firstVisibleItemPositions)
                }
                is LinearLayoutManager -> {
                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                }
            }

            if (mReverse) {
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = FIRST_PAGE_INDEX
                    this.previousTotalItemCount = totalItemCount
                    if (totalItemCount == 0) {
                        this.loading = true
                    }
                }
                if (loading && totalItemCount - LOADING_ITEM > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                if (!loading
                    && totalItemCount > 10 //Teste
                    && firstVisibleItemPosition < visibleThreshold) {
                    currentPage++
                    mOnScrollCallback?.onLoadMore(currentPage, totalItemCount)
                    loading = true
                }
            } else {
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = FIRST_PAGE_INDEX
                    this.previousTotalItemCount = totalItemCount
                    if (totalItemCount == 0) {
                        this.loading = true
                    }
                }
                if (loading && totalItemCount + LOADING_ITEM > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                    currentPage++
                    mOnScrollCallback?.onLoadMore(currentPage, totalItemCount)
                    loading = true
                }
            }

            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                scrolledDistance = 0
                controlsVisible = false

                mOnScrollCallback?.onHide()
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                scrolledDistance = 0
                controlsVisible = true

                mOnScrollCallback?.onShow()
            }

            if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
                scrolledDistance += dy
            }
        }
    }

    // Call whenever performing new searches
    fun reset() {
        currentPage = FIRST_PAGE_INDEX
        previousTotalItemCount = 1

        loading = true
        controlsVisible = true
    }

}