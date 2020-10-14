package br.com.isgreen.archandroid.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.common.LoadingViewHolder
import br.com.isgreen.archandroid.util.OnItemClickListener

/**
 * Created by Éverdes Soares on 04/02/2020.
 */

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDataList: MutableList<T> by lazy { mutableListOf<T>() }

    private var mIsLoading = false
    private var mLoadingPosition = RecyclerView.NO_POSITION

    var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        onCreateViewHolderBase(LayoutInflater.from(parent.context), parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        this.onBindViewHolderBase(holder, position)

        if (holder is LoadingViewHolder) {
            return
        }

//        if (holder is HeaderViewHolder) {
//            return
//        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(
                it, holder.adapterPosition,
                getItem(holder.adapterPosition)
            )
        }
    }

    abstract fun onCreateViewHolderBase(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract fun <VH : RecyclerView.ViewHolder> onBindViewHolderBase(
        holder: VH,
        position: Int
    )

    override fun getItemCount() = mDataList.size

    protected fun getItem(index: Int) = mDataList[index]

    val data: MutableList<T>
        get() = mDataList

    val lastIndex: Int
        get() = mDataList.lastIndex

    val isEmpty: Boolean
        get() = data.isEmpty()

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

        this.mDataList.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun clearData() {
        this.mDataList.clear()
        this.notifyDataSetChanged()
    }

    fun addData(list: List<T>) {
        val firstItemPosition = this.mDataList.lastIndex + 1
        this.mDataList.addAll(list)
        this.notifyItemRangeInserted(firstItemPosition, list.size)
    }

    fun addData(position: Int, list: List<T>) {
        this.mDataList.addAll(position, list)
        this.notifyItemRangeInserted(position, list.size)
    }

    fun removeData(list: List<T>) {
        this.mDataList.removeAll(list)
        this.notifyDataSetChanged()
    }

    fun showLoading(showInBottom: Boolean) {
        if (itemCount == 0 || mIsLoading) {
            return
        }

        mIsLoading = true

        mLoadingPosition = if (showInBottom) {
            addItem(getItem(0))
            lastIndex
        } else {
            addItem(0, getItem(0))
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

    fun isLoading(): Boolean {
        return mIsLoading
    }
}