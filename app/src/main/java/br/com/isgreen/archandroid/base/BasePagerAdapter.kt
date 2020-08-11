package br.com.isgreen.archandroid.base

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Éverdes Soares on 08/10/2020.
 */

class BasePagerAdapter(
    private val context: Context?,
    fragmentManager: FragmentManager,
    private val fragments: List<Fragment>,
    @ArrayRes private val pageTitles: Int
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context?.resources?.getStringArray(pageTitles)?.get(position)
    }
}