package br.com.isgreen.archandroid.screen.repo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseAdapter
import br.com.isgreen.archandroid.data.model.repository.Repo
import br.com.isgreen.archandroid.extension.loadImageRounded
import br.com.isgreen.archandroid.extension.setDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_repo_item.view.*

/**
 * Created by Éverdes Soares on 09/22/2019.
 */

class RepoAdapter : BaseAdapter<Repo>() {

    override fun onCreateViewHolderBase(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return RepoViewHolder(
            LayoutInflater.from(parent?.context)
                .inflate(R.layout.fragment_repo_item, parent, false)
        )
    }

    override fun <VH : RecyclerView.ViewHolder> onBindViewHolderBase(holder: VH, position: Int) {
        setDataView(holder as RepoViewHolder, data[position])
    }

    private fun setDataView(holder: RepoViewHolder, repo: Repo) {
        holder.itemView.apply {
            imgIcon?.loadImageRounded(repo.links?.avatar?.href)
            txtDate.setDate(R.string.created_on, repo.createdOn?.substring(0, 19))
            txtRepository.text = repo.name
        }
    }

    inner class RepoViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}