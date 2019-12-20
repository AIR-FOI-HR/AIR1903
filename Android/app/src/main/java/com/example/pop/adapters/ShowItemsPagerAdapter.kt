package com.example.pop.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pop.fragments.PackagesFragment
import com.example.pop.fragments.ProductsFragment

class PagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                ProductsFragment()
            }else -> {
                return PackagesFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Products"
            else -> {
                return "Packages"
            }
        }
    }

}