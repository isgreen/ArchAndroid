package br.com.isgreen.archandroid.screen.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseAdapter
import br.com.isgreen.archandroid.data.model.theme.Theme
import br.com.isgreen.archandroid.databinding.FragmentThemeItemBinding

/**
 * Created by Éverdes Soares on 04/14/2020.
 */

class ThemeAdapter : BaseAdapter<Theme>() {

    private var mCheckedPosition = 0

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ThemeViewHolder(
            FragmentThemeItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setDataView(
            theme = data[position],
            holder = holder as ThemeViewHolder,
            isChecked = mCheckedPosition == position
        )
    }

    private fun setDataView(holder: ThemeViewHolder, theme: Theme, isChecked: Boolean) {
        holder.binding.apply {
            txtThemeName.let {
                it.setText(theme.name)
                val right = if (isChecked) R.drawable.ic_check else 0
                it.setCompoundDrawablesWithIntrinsicBounds(theme.icon, 0, right, 0)
            }
        }
    }

    fun setCheckedPosition(position: Int) {
        notifyItemChanged(mCheckedPosition)
        mCheckedPosition = position
        notifyItemChanged(mCheckedPosition)
    }

    inner class ThemeViewHolder(
        val binding: FragmentThemeItemBinding
    ) : BaseViewHolder(binding.root)
}