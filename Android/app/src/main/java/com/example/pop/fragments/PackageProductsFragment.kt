package com.example.pop.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.pop.R
import kotlinx.android.synthetic.main.fragment_package_products.view.*

class PackageProductsAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_package_products_adding, container, false)

        view.layoutPackageProductsListingButtonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_package_add_products_to_package_list_products)
        }

        return view
    }

}
