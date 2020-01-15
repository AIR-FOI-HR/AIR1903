package com.example.pop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pop.adapters.ShowItemsPagerAdapter
import com.example.pop.fragments.PackagesFragment
import com.example.pop.fragments.ProductsFragment
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.activity_tab_layout.*

class ShowItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)

        val fragmentPagerAdapter = ShowItemsPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentPagerAdapter

        tabLayout.setupWithViewPager(viewPager)


        layoutTabLayoutButtonSell.setOnClickListener { startSellActivity() }
    }

    private fun startSellActivity() {
        var selectedItems : ArrayList<Item> = arrayListOf()
        val fragmentAdapter = viewPager.adapter as ShowItemsPagerAdapter
        selectedItems.addAll(fragmentAdapter.packagesFragment.getRecyclerAdapter().getSelectedItems())
        selectedItems.addAll(fragmentAdapter.productsFragment.getRecyclerAdapter().getSelectedItems())

        val intent = Intent(this, SellItemsActivity::class.java)
        intent.putExtra("items", ItemsWrapper(selectedItems))
        startActivity(intent)
    }
}
