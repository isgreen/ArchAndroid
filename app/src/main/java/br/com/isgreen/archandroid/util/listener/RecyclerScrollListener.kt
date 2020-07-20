package br.com.isgreen.archandroid.util.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Éverdes Soares on 05/12/2020.
 */

@Deprecated("Replaced by OnRecyclerViewScrollListener")
class RecyclerScrollListener : RecyclerView.OnScrollListener {

    companion object {
        const val LOADING_ITEM = 1
        const val HIDE_THRESHOLD = 20
        const val FIRST_PAGE_INDEX = 1
    }

    // The minimum amount of items to have below your current scroll position
    // before mIsLoading more.
    private var mCurrentDy = 0
    private var mCurrentPage = 1
    private var mVisibleThreshold = 5
    private var scrolledDistance = 0
    private var previousTotalItemCount = 0
    private var mCurrentLastVisibleItemPosition = RecyclerView.NO_POSITION

    private var mIsLoading = true
    private var mIsListenBothWays = false
    private var mIsReverse: Boolean? = false
    private var mIsScrollListenerEnabled = false
    private var mIsControlsVisible: Boolean = false

    private var mOnScrollCallback: OnScrollCallback? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    constructor(layoutManager: LinearLayoutManager?, onScrollCallback: OnScrollCallback) : super() {
        mLayoutManager = layoutManager
        mOnScrollCallback = onScrollCallback
    }

    /*
     * If "listenBothWays" is true "reverse" value will be ignored in onScrolled method
     */
    constructor(
        onScrollCallback: OnScrollCallback,
        layoutManager: LinearLayoutManager?,
        visibleThreshold: Int,
        listenBothWays: Boolean,
        reverse: Boolean? = false
    ) : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
        mVisibleThreshold = visibleThreshold
        mIsReverse = reverse
        mIsListenBothWays = listenBothWays
    }

    constructor(
        onScrollCallback: OnScrollCallback,
        layoutManager: GridLayoutManager?,
        reverse: Boolean
    ) : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
        mVisibleThreshold *= layoutManager?.spanCount ?: 1
        mIsReverse = reverse
    }

    constructor(
        onScrollCallback: OnScrollCallback,
        layoutManager: StaggeredGridLayoutManager?,
        reverse: Boolean
    ) : super() {
        mOnScrollCallback = onScrollCallback
        mLayoutManager = layoutManager
        mVisibleThreshold *= layoutManager?.spanCount ?: 1
        mIsReverse = reverse
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

    private fun findFirstVisibleItemPosition(): Int {
        mLayoutManager?.let { layoutManager ->
            return when (layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findFirstVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    layoutManager.findFirstVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    val firstVisibleItemPositions =
                        layoutManager.findFirstVisibleItemPositions(null)
                    getFirstVisibleItem(firstVisibleItemPositions)
                }
                else -> 0
            }
        }

        return 0
    }

    private fun findLastVisibleItemPosition(): Int {
        mLayoutManager?.let { layoutManager ->
            return when (layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    val lastVisibleItemPositions =
                        layoutManager.findLastVisibleItemPositions(null)
                    getLastVisibleItem(lastVisibleItemPositions)
                }
                else -> 0
            }
        }

        return 0
    }

    private fun onScrolledToFirstItem() {
        mLayoutManager?.let { layoutManager ->
            val firstVisibleItemPosition = findFirstVisibleItemPosition()

            val totalItemCount = layoutManager.itemCount
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.mCurrentPage = FIRST_PAGE_INDEX
                this.previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    this.mIsLoading = true
                }
            }
            // If it’s still mIsLoading, we check to see if the dataset count has
            // changed, if so we conclude it has finished mIsLoading and update the current page
            // number and total item count.
            if (mIsLoading && totalItemCount - LOADING_ITEM > previousTotalItemCount) {
                mIsLoading = false
                previousTotalItemCount = totalItemCount
            }

            // If it isn’t currently mIsLoading, we check to see if we have breached
            // the mVisibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onScrollPage to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!mIsLoading
                && totalItemCount > 10 //Test
                && firstVisibleItemPosition < mVisibleThreshold
            ) {
                mCurrentPage++
                mOnScrollCallback?.onScrolledToFirstItem()
                mOnScrollCallback?.onScrollPage(mCurrentPage, totalItemCount)
                mIsLoading = true
            }
        }
    }

    private fun onScrolledToLastItem() {
        mLayoutManager?.let { layoutManager ->
            val lastVisibleItemPosition = findLastVisibleItemPosition()

            val totalItemCount = layoutManager.itemCount
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.mCurrentPage = FIRST_PAGE_INDEX
                this.previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    this.mIsLoading = true
                }
            }
            // If it’s still mIsLoading, we check to see if the dataset count has
            // changed, if so we conclude it has finished mIsLoading and update the current page
            // number and total item count.
//            if (mIsLoading && totalItemCount - LOADING_ITEM > previousTotalItemCount) {
            if (mIsLoading && totalItemCount != previousTotalItemCount) {
                mIsLoading = false
                previousTotalItemCount = totalItemCount
            }

            // If it isn’t currently mIsLoading, we check to see if we have breached
            // the mVisibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onScrollPage to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!mIsLoading
                && totalItemCount > 0
                && lastVisibleItemPosition + mVisibleThreshold > totalItemCount
            ) {
                mIsLoading = true
                mCurrentPage++
                mOnScrollCallback?.onScrolledToLastItem()
                mOnScrollCallback?.onScrollPage(mCurrentPage, totalItemCount)
            }
        }
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
//        if (!mIsScrollListenerEnabled) {
//            return
//        }

        val lastVisibleItemPosition = findLastVisibleItemPosition()
        if (lastVisibleItemPosition != RecyclerView.NO_POSITION
            && lastVisibleItemPosition != mCurrentLastVisibleItemPosition
        ) {
            mOnScrollCallback?.onScrolled(lastVisibleItemPosition)
            mCurrentLastVisibleItemPosition = lastVisibleItemPosition
        }

//        if (mIsListenBothWays) {
//            val isScrollingToFirst = dy < mCurrentDy
//
//            if (isScrollingToFirst) {
//                onScrolledToFirstItem()
//            } else {
//                onScrolledToLastItem()
//            }
//        } else {
            if (/*mIsReverse == true && */dy < mCurrentDy) {
                onScrolledToFirstItem()
            } else {
                onScrolledToLastItem()
            }
//        }

        mCurrentDy = dy

        if (scrolledDistance > HIDE_THRESHOLD && mIsControlsVisible) {
            scrolledDistance = 0
            mIsControlsVisible = false

            mOnScrollCallback?.onHide()
        } else if (scrolledDistance < -HIDE_THRESHOLD && !mIsControlsVisible) {
            scrolledDistance = 0
            mIsControlsVisible = true

            mOnScrollCallback?.onShow()
        }

        if (mIsControlsVisible && dy > 0 || !mIsControlsVisible && dy < 0) {
            scrolledDistance += dy
        }
    }

    fun setScrollListenerEnabled(isScrollListenerEnabled: Boolean) {
        mIsScrollListenerEnabled = isScrollListenerEnabled
    }

    // Call whenever performing new searches
    fun reset() {
        mCurrentPage =
            FIRST_PAGE_INDEX
        previousTotalItemCount = 1

        mIsLoading = true
        mIsControlsVisible = true
    }

}