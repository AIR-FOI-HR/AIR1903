package com.example.pop.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pop.R
import com.example.pop.adapters.ProductRecyclerAdapter

/**
 * A simple [Fragment] subclass.
 */
class ProductsTabFragment : Fragment() {
    private lateinit var productAdapter: ProductRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_tab, container, false)
    }
}
