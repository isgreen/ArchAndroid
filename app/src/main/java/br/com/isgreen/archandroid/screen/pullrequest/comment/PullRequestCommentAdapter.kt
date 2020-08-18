package br.com.isgreen.archandroid.screen.pullrequest.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseAdapter
import br.com.isgreen.archandroid.common.LoadingViewHolder
import br.com.isgreen.archandroid.data.model.comment.Comment
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.setDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_pull_request_comment_item.view.*

/**
 * Created by Éverdes Soares on 08/17/2020.
 */

class PullRequestCommentAdapter : BaseAdapter<Comment>() {

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
            PullRequestCommentViewHolder(inflater.inflate(R.layout.fragment_pull_request_comment_item, parent, false))
        }
    }

    override fun <VH : RecyclerView.ViewHolder> onBindViewHolderBase(holder: VH, position: Int) {
        if (holder is PullRequestCommentViewHolder) {
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

    private fun setDataView(holder: PullRequestCommentViewHolder, comment: Comment?) {
        holder.itemView.apply {
            txtCommentMessage?.text = comment?.content?.raw
            txtCommentDate?.setDate(R.string.created_on, comment?.createdOn?.substring(0, 19))
            imgCommentUserAvatar?.loadImageRounded(comment?.user?.links?.avatar?.href, R.drawable.ic_user, R.dimen.margin_small)
            txtCommentUserName?.text = context?.getString(R.string.created_by, comment?.user?.displayName)
        }
    }

    inner class PullRequestCommentViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}