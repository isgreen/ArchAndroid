package br.com.isgreen.archandroid.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.data.model.adapter.AdapterItem
import br.com.isgreen.archandroid.util.OnItemClickListener

/**
 * Created by Éverdes Soares on 04/02/2020.
 */

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_LOADING = 2
    }

    private val mDataList: MutableList<T> by lazy { mutableListOf<T>() }

    private var mIsLoading = false
    private var mLoadingPosition = RecyclerView.NO_POSITION
    private var mLastItemClickedPosition = RecyclerView.NO_POSITION

    val data: MutableList<T> get() = mDataList
    val isEmpty: Boolean get() = data.isEmpty()
    val lastIndex: Int get() = mDataList.lastIndex
    var lastItemClickedPosition: Int
        get() {
            if (mLastItemClickedPosition == RecyclerView.NO_POSITION) {
                throw IllegalStateException(
                    "The position of the clicked item is not being saved, to use this variable, " +
                            "it is necessary that your ViewHolder extends the BaseViewHolder, " +
                            "in this class, or in your ViewHolder, " +
                            "manually set the lastItemClickedPosition in the click listener."
                )
            }

            return mLastItemClickedPosition
        }
        set(value) {
            mLastItemClickedPosition = value
        }

    var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        onCreateViewHolder(LayoutInflater.from(parent.context), parent, viewType)

    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder

    override fun getItemCount() = mDataList.size

    protected fun getItem(index: Int) = mDataList[index]

    fun getItemRange(startIndex: Int) = getItemRange(startIndex, mDataList.size)

    fun getItemRange(startIndex: Int, endIndex: Int) = mDataList.subList(startIndex, endIndex)

    fun addItem(item: T) {
        mDataList.add(item)
        notifyItemInserted(if (itemCount > 0) mDataList.lastIndex else 0)
    }

    fun addItem(position: Int, item: T) {
        mDataList.add(position, item)
        notifyItemInserted(position)
    }

    fun setItem(position: Int, item: T?) {
        if (item == null) {
            return
        }

        if (position > RecyclerView.NO_POSITION) {
            mDataList[position] = item
        } else {
            mDataList.add(item)
        }
//        notifyItemInserted(if (itemCount > 0) position else 0)
        notifyDataSetChanged()
    }

    fun changeItem(position: Int, item: T?) {
        item?.let {
            mDataList[position] = item
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (mDataList.isEmpty()) {
            return
        }

        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearData() {
        mDataList.clear()
        notifyDataSetChanged()
    }

    fun addData(list: List<T>) {
        val firstItemPosition = mDataList.lastIndex + 1
        mDataList.addAll(list)
        notifyItemRangeInserted(firstItemPosition, list.size)
    }

    fun addData(position: Int, list: List<T>) {
        mDataList.addAll(position, list)
        notifyItemRangeInserted(position, list.size)
    }

    fun setData(list: List<T>) {
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeData(list: List<T>) {
        mDataList.removeAll(list)
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun addHeader(position: Int) {
        val header = AdapterItem(VIEW_TYPE_HEADER)
        mDataList.add(position, header as T)
        notifyItemInserted(position)
    }

    @Suppress("UNCHECKED_CAST")
    fun showLoading(showInBottom: Boolean) {
        if (itemCount == 0 || mIsLoading) {
            return
        }

        mIsLoading = true
        val loadingItem = AdapterItem(VIEW_TYPE_LOADING) as T

        mLoadingPosition = if (showInBottom) {
            addItem(loadingItem)
            lastIndex
        } else {
            addItem(0, loadingItem)
            0
        }
    }

    fun hideLoading() {
        if (itemCount == 0 || !mIsLoading || mLoadingPosition == RecyclerView.NO_POSITION) {
            return
        }

        mIsLoading = false

        mDataList.removeAt(mLoadingPosition)
        notifyItemRemoved(mLoadingPosition)

        mLoadingPosition = RecyclerView.NO_POSITION
    }

    fun isLoading(): Boolean = mIsLoading

    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            onItemClickListener?.let {
                itemView.setOnClickListener {
                    mLastItemClickedPosition = adapterPosition
                    onItemClickListener?.invoke(it, adapterPosition, getItem(adapterPosition))
                }
            }
        }
    }
}