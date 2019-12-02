package com.example.pop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.adapters.DataSource
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.pop.adapters.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_show_products.*


class ShowProductsActivity : AppCompatActivity(){


    private lateinit var blogAdapter: ProductRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)

        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet(){
        val data = DataSource.createDataSet()
        blogAdapter.submitList(data)
    }

    private fun initRecyclerView(){

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ShowProductsActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            blogAdapter = ProductRecyclerAdapter()
            adapter = blogAdapter
        }
    }

}
