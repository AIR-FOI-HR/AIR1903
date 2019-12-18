package com.example.pop.adapters

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pop.PackagesTabFragment
import com.example.pop.ProductsTabFragment

class PagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                ProductsTabFragment()
            }else -> {
                return PackagesTabFragment()
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