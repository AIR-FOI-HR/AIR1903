package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.R
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.fragment_package_products.*
import kotlinx.android.synthetic.main.fragment_package_products.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageProductsFragment : Fragment() {

    private lateinit var productAdapter: ProductRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_package_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPackageProductsListingButtonAdd.setOnClickListener {
            addProducts()
            //it.findNavController().navigate(R.id.action_package_add_products_to_package_list_products)
        }

        productAdapter = ProductRecyclerAdapter(context)
        getProducts()
        package_product_list.adapter = productAdapter
        package_product_list.layoutManager = LinearLayoutManager(context)
    }

    private fun addProducts(){
        val api = Common.api
        for(product in productAdapter.products){
            if(product.Kolicina != "0")
                api.addToPackage(Session.user.Token, true, "41", product.Id.toString(), product.Kolicina )
        }
    }

    private fun getProducts(){
        val api = Common.api
        api.getProducts(true, Session.user.Token, Session.user.KorisnickoIme).enqueue(object :
            Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                val resp = response.body()!!.DATA

                when {
                    response.body()!!.STATUSMESSAGE=="OLD TOKEN" -> {
                        val intent = Intent(activity, LoginActivity::class.java)
                        Toast.makeText(context, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        activity?.finishAffinity()
                    }
                    response.body()!!.STATUSMESSAGE=="OK" -> {}
                    else -> Toast.makeText(context, response.body()!!.STATUSMESSAGE, Toast.LENGTH_LONG).show()
                }

                if (!resp.isNullOrEmpty()){
                    resp.forEach { it.Kolicina = "0" }
                    productAdapter.submitList(resp)
                }
            }
        })
    }
}
