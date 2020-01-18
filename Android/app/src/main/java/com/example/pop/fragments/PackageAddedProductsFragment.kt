package com.example.pop.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.R
import com.example.pop.ShowItemsActivity
import com.example.pop.adapters.AddedProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.PackageResponse
import com.example.webservice.Model.Product
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.fragment_package_added_products.*
import kotlinx.android.synthetic.main.fragment_package_added_products.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageAddedProductsFragment : Fragment() {

    private lateinit var productAdapter: AddedProductRecyclerAdapter
    var items = ArrayList<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_package_added_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.layoutPackageProductsListingButtonAdd.setOnClickListener {
            transitionToAddProducts(it)
        }

        view.btn_submit_package_products.setOnClickListener{
            submitPackage()
        }
        try {
            items = arguments!!.get("prods") as ArrayList<Product>
        }catch (e:kotlin.KotlinNullPointerException){
            try {
                items = (activity!!.intent.extras!!.get("item") as PackageClass).Items as ArrayList<Product>
            } catch (e:TypeCastException){
                items=ArrayList<Product>()
            }
        }

        productAdapter = AddedProductRecyclerAdapter(context)
        package_added_product_list.adapter = productAdapter
        productAdapter.submitList(ArrayList(items))

        package_added_product_list.layoutManager = LinearLayoutManager(context)
    }



    private fun submitPackage(){
        var id=0
        try {
            id = (activity!!.intent.extras!!.get("item") as PackageClass).Id!!
            println("DEBUG33-try-"+id)
        } catch (e:kotlin.TypeCastException){
            //id = arguments!!.getInt("packageId")
            id=(activity!!.intent.extras!!.getInt("packetId"))
            println("DEBUG33-catch-"+id)
        }

        var prodIds = ArrayList<Int>()
        var prodAmt = ArrayList<String>()
        for (i:Product in productAdapter.getItems()){
            prodIds.add(i.Id!!)
            prodAmt.add(i.Kolicina)
        }
        val api = Common.api
        api.addToPackage(Session.user.Token, true, id.toString(), prodIds,prodAmt).enqueue(object:Callback<PackageResponse> {
            override fun onFailure(call: Call<PackageResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PackageResponse>, response: Response<PackageResponse>) {
                val resp = response.body()!!.DATA

                when {
                    response.body()!!.STATUSMESSAGE=="OLD TOKEN" -> {
                        val intent = Intent(activity, LoginActivity::class.java)
                        Toast.makeText(context, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        activity?.finishAffinity()
                    }
                    response.body()!!.STATUSMESSAGE=="PRODUCT ADDED TO PACKET" -> {
                        val intent = Intent(activity, ShowItemsActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        activity!!.startActivity(intent)
                        (activity as Activity).overridePendingTransition(0, 0)
                        (activity as Activity).finish()
                        (activity as Activity).overridePendingTransition(0, 0)
                    }
                    else -> Toast.makeText(context, response.body()!!.STATUSMESSAGE, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun transitionToAddProducts(it:View){
        var alreadyPresentBundle = bundleOf(
            "items" to items
        )
        it.findNavController().navigate(R.id.action_packageProductsListing_to_packageProductsAdding, alreadyPresentBundle)
    }
}
