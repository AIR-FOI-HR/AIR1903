package com.example.pop.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.R
import com.example.pop.adapters.AddedProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.fragment_package_added_products.*
import kotlinx.android.synthetic.main.fragment_package_added_products.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageAddedProductsFragment : Fragment() {

    private lateinit var productAdapter: AddedProductRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_package_added_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.layoutPackageProductsListingButtonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_packageProductsListing_to_packageProductsAdding)
        }

        view.btn_submit_package_products.setOnClickListener{
            submitPackage()
        }

        productAdapter = AddedProductRecyclerAdapter(context)
        package_added_product_list.adapter = productAdapter
        getProducts()
        package_added_product_list.layoutManager = LinearLayoutManager(context)
    }


    private fun getProducts(){
        val api = Common.api
        lateinit var id: String
        if (arguments != null) {
            var packageClass = activity!!.intent.getSerializableExtra("item") as PackageClass
            id = packageClass.Id.toString()
            println("DEBUG33-"+arguments!!.getInt("packageId"))
        }

        api.getOnePackageContents(Session.user.Token, true, id).enqueue(object:Callback<ProductResponse>{
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

                if (resp != null) productAdapter.submitList(resp)
            }
        }
        )
     //val products: ArrayList<Product> = arguments?.getSerializable("addedProducts")
    }

    private fun submitPackage(){

    }
}
