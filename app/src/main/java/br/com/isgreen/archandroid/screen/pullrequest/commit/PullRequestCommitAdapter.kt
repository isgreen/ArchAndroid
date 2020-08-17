package br.com.isgreen.archandroid.screen.pullrequest.commit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseAdapter
import br.com.isgreen.archandroid.common.LoadingViewHolder
import br.com.isgreen.archandroid.data.model.commit.Commit
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.setDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_pull_request_commit_item.view.*

/**
 * Created by Éverdes Soares on 08/12/2020.
 */

class PullRequestCommitAdapter : BaseAdapter<Commit>() {

    companion object {
        const val ITEM_TYPE = 0
        const val LOADING_TYPE = 1
    }

    override fun onCreateViewHolderBase(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == LOADING_TYPE) {
            LoadingViewHolder(inflater.inflate(R.layout.loading_item, parent, false))
        } else {
            PullRequestCommitViewHolder(inflater.inflate(R.layout.fragment_pull_request_commit_item, parent, false))
        }
    }

    override fun <VH : RecyclerView.ViewHolder> onBindViewHolderBase(holder: VH, position: Int) {
        if (holder is PullRequestCommitViewHolder) {
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

    private fun setDataView(holder: PullRequestCommitViewHolder, commit: Commit?) {
        holder.itemView.apply {
            txtCommitMessage?.text = commit?.message
            txtCommitDate?.setDate(R.string.created_on, commit?.date?.substring(0, 19))
            imgCommitUserAvatar?.loadImageRounded(commit?.author?.user?.links?.avatar?.href, R.drawable.ic_user, R.dimen.margin_small)
            txtCommitUserName?.text = context?.getString(R.string.created_by, commit?.author?.user?.displayName)
        }
    }

    inner class PullRequestCommitViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}