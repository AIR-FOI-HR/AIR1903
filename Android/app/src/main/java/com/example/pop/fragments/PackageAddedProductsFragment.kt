package com.example.pop.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.ManagePackagesActivity
import com.example.pop.R
import com.example.pop.adapters.AddedProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.*
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.fragment_package_added_products.*
import kotlinx.android.synthetic.main.fragment_package_added_products.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageAddedProductsFragment : Fragment() {

    private lateinit var productAdapter: AddedProductRecyclerAdapter
    var parentActivity: ManagePackagesActivity = ManagePackagesActivity()
    private var products: ArrayList<Product>? = ArrayList()
    private lateinit var appContext:Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext=activity as Context

    }

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

        val parentActivity: ManagePackagesActivity = activity as ManagePackagesActivity

        /*view.btn_submit_package_products.setOnClickListener{
            submitPackage()
        }*/

        productAdapter = AddedProductRecyclerAdapter(context)
        getProducts()
        package_added_product_list.adapter = productAdapter
        package_added_product_list.layoutManager = LinearLayoutManager(context)
        Log.e("Paket ID",parentActivity.packageId.toString())
    }

    private fun getProducts(){
        val api = Common.api
        api.getOnePackageContents(
            Session.user.Token,
            true,
            parentActivity.packageId.toString()
        ).enqueue(object :
            Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(appContext, t.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.body()!!.STATUSMESSAGE == "SUCCESS") {
                    products = response.body()!!.DATA
                } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                    val intent = Intent(appContext, LoginActivity::class.java)
                    Toast.makeText(
                        appContext,
                        "Sesija istekla, molimo prijavite se ponovno",
                        Toast.LENGTH_LONG
                    ).show()
                    Session.reset()
                    startActivity(intent)
                    ActivityCompat.finishAffinity(appContext as Activity)
                } else
                    Toast.makeText(
                        appContext,
                        response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT
                    ).show()
            }
        })
        products!!.forEach {
            Log.e("Proizvodi paketa:", it.Naziv)
        }
        productAdapter.submitList(products!!)
    }
}
