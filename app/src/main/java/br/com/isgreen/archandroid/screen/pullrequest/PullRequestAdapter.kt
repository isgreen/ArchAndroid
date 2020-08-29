package br.com.isgreen.archandroid.screen.pullrequest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseAdapter
import br.com.isgreen.archandroid.common.LoadingViewHolder
import br.com.isgreen.archandroid.data.model.pullrequest.PullRequest
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.setDate
import br.com.isgreen.archandroid.util.OnInnerViewItemClickListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_pull_request_item.*
import kotlinx.android.synthetic.main.fragment_pull_request_item.view.*

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
            LoadingViewHolder(inflater.inflate(R.layout.loading_item, parent, false))
        } else {
            PullRequestViewHolder(inflater.inflate(R.layout.fragment_pull_request_item, parent, false))
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
        holder.itemView.apply {
            imgRepositoryIcon?.loadImageRounded(pullRequest?.author?.links?.avatar?.href, R.drawable.ic_user, R.dimen.margin_small)
            txtPullRequestCreatedDate?.setDate(R.string.created_on, pullRequest?.createdOn?.substring(0, 19))
            txtRepositoryName?.text = pullRequest?.source?.repository?.name
            txtPullRequestAuthor?.text = context?.getString(R.string.created_by, pullRequest?.author?.displayName)
        }
    }

    inner class PullRequestViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            imgPullRequestMenu?.setOnClickListener {
                val pullRequest = data[adapterPosition]
                onInnerViewItemClickListener?.invoke(it, adapterPosition, pullRequest)
            }
        }
    }
}