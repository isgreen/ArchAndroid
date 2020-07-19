package br.com.isgreen.archandroid.screen.repo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.setDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_repo_item.view.*

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoAdapter : PagedListAdapter<Repo, RepoAdapter.RepoViewHolder>(DIFF_CALLBACK){//BaseAdapter<Repo>() {

    companion object {
        const val ITEM_TYPE = 0
        const val LOADING_TYPE = 1

        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Repo>() {

            override fun areItemsTheSame(oldRepo: Repo, newRepo: Repo) = oldRepo.uuid == newRepo.uuid
            override fun areContentsTheSame(oldRepo: Repo, newRepo: Repo) = oldRepo == newRepo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
//        return if (viewType == LOADING_TYPE) {
//            LoadingViewHolder(inflater.inflate(R.layout.loading_item, parent, false))
//        } else {
//            RepoViewHolder(inflater.inflate(R.layout.fragment_repo_item, parent, false))
//        }
        return RepoViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_repo_item, parent, false))
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        setDataView(holder, getItem(position))
    }

//    override fun onCreateViewHolderBase(
//        inflater: LayoutInflater,
//        parent: ViewGroup?,
//        viewType: Int
//    ): RecyclerView.ViewHolder {
//        return if (viewType == LOADING_TYPE) {
//            LoadingViewHolder(inflater.inflate(R.layout.loading_item, parent, false))
//        } else {
//            RepoViewHolder(inflater.inflate(R.layout.fragment_repo_item, parent, false))
//        }
//    }
//
//    override fun <VH : RecyclerView.ViewHolder> onBindViewHolderBase(holder: VH, position: Int) {
//        if (holder is RepoViewHolder) {
//            setDataView(holder, data[position])
//        }
//    }

//    override fun getItemViewType(position: Int): Int {
//        return if (position == lastIndex && isLoading()) {
//            LOADING_TYPE
//        } else {
//            ITEM_TYPE
//        }
//    }

    private fun setDataView(holder: RepoViewHolder, repo: Repo?) {
        holder.itemView.apply {
            imgIcon?.loadImageRounded(repo?.links?.avatar?.href)
            txtDate.setDate(R.string.created_on, repo?.createdOn?.substring(0, 19))
            txtRepository.text = repo?.name
        }
    }

    inner class RepoViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}