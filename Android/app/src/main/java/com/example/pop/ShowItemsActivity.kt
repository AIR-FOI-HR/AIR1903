package com.example.pop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.core.BaseActivity
import com.example.pop.adapters.ShowItemsPagerAdapter
import com.example.webservice.Model.Item
import kotlinx.android.synthetic.main.activity_tab_layout.*


class ShowItemsActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)

        val fragmentPagerAdapter = ShowItemsPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        btn_sell.setOnClickListener { startSellActivity() }
        btn_new_item.setOnClickListener { addItem(viewPager.currentItem) }
    }

    private fun addItem(currentFragment: Int) {
        val fragmentAdapter = viewPager.adapter as ShowItemsPagerAdapter
        if(currentFragment == 0)
            fragmentAdapter.productsFragment.addProduct()
        else
            fragmentAdapter.packagesFragment.addPackage()
    }

    private fun startSellActivity() {
        val selectedItems : ArrayList<Item> = arrayListOf()
        val fragmentAdapter = viewPager.adapter as ShowItemsPagerAdapter
        selectedItems.addAll(fragmentAdapter.packagesFragment.selectedPackages)
        selectedItems.addAll(fragmentAdapter.productsFragment.selectedProducts)

        if(selectedItems.isNotEmpty()) {
            val intent = Intent(this, SellItemsActivity::class.java)
            intent.putExtra("items", ItemsWrapper(selectedItems))
            startActivity(intent)
        } else {
            Toast.makeText(this@ShowItemsActivity, getString(R.string.toast_no_items_selected),Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuSeller::class.java)
        startActivity(intent)
        finishAffinity()
    }

}
