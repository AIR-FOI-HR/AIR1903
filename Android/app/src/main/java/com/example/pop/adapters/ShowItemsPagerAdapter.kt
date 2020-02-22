package com.example.pop.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pop.fragments.PackagesFragment
import com.example.pop.fragments.ProductsFragment
import com.example.pop_sajamv2.Session

class ShowItemsPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    lateinit var productsFragment : ProductsFragment
    lateinit var packagesFragment: PackagesFragment
    override fun getItem(position: Int): Fragment {
        when (position){
            0 -> {
                productsFragment = ProductsFragment()
                return productsFragment
            }else -> {
                packagesFragment = PackagesFragment()
                return packagesFragment
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 ->{
                if (Session.user.Jezik==1) Session.productsHrv
                else Session.productsEng
            }
            1 -> {
                if (Session.user.Jezik==1) Session.packagesHrv
                else Session.packagesEng
            }
            else -> {
                null
            }
        }
    }

}