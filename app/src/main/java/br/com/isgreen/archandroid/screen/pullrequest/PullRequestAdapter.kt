package br.com.isgreen.archandroid.screen.pullrequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseAdapter
import br.com.isgreen.archandroid.common.LoadingViewHolder
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.databinding.FragmentPullRequestItemBinding
import br.com.isgreen.archandroid.databinding.LoadingItemBinding
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.putTextColor
import br.com.isgreen.archandroid.extension.setBackgroundDrawableColor
import br.com.isgreen.archandroid.extension.setDate
import br.com.isgreen.archandroid.util.OnInnerViewItemClickListener

/**
 * Created by Éverdes Soares on 08/07/2020.
 */

class PullRequestAdapter : BaseAdapter<PullRequest>() {

    companion object {
        const val ITEM_TYPE = 0
        const val LOADING_TYPE = 1
    }

    var onInnerViewItemClickListener: OnInnerViewItemClickListener? = null

    override fun onCreateViewHolderBase(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == LOADING_TYPE) {
            LoadingViewHolder(
                LoadingItemBinding.inflate(inflater, parent, false)
            )
        } else {
            PullRequestViewHolder(
                FragmentPullRequestItemBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun <VH : RecyclerView.ViewHolder> onBindViewHolderBase(holder: VH, position: Int) {
        if (holder is PullRequestViewHolder) {
            setDataView(holder, data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == lastIndex && isLoading()) {
            LOADING_TYPE
        } else {
            ITEM_TYPE
        }
    }

    private fun setDataView(holder: PullRequestViewHolder, pullRequest: PullRequest?) {
        holder.binding.apply {
            imgRepositoryIcon.loadImageRounded(pullRequest?.author?.links?.avatar?.href, R.drawable.ic_user, R.dimen.margin_small)
            txtPullRequestCreatedDate.setDate(R.string.on_date, pullRequest?.createdOn?.substring(0, 19))
            txtRepositoryName.text = pullRequest?.source?.repository?.name
            txtPullRequestAuthor.text = this.root.context?.getString(R.string.created_by, pullRequest?.author?.displayName)
            txtPullRequestStatus.text = pullRequest?.state?.value

            val color = pullRequest?.stateColor ?: R.color.red
            txtPullRequestStatus.putTextColor(color)
            txtPullRequestStatus.setBackgroundDrawableColor(color)
        }
    }

    inner class PullRequestViewHolder(val binding: FragmentPullRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgPullRequestMenu.setOnClickListener {
                val pullRequest = data[adapterPosition]
                onInnerViewItemClickListener?.invoke(it, adapterPosition, pullRequest)
            }
        }
    }
}