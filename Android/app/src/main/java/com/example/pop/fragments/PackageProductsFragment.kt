package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.ManagePackagesActivity
import com.example.pop.R
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.PackageResponse
import com.example.webservice.Model.Product
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.fragment_package_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackageProductsFragment : Fragment() {

    private lateinit var productAdapter: ProductRecyclerAdapter
    var parentActivity: ManagePackagesActivity = ManagePackagesActivity()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_package_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPackageProductsListingButtonAdd.setOnClickListener {
            addProducts(it)
        }

        getProducts()

    }

    private fun addProducts(it:View){
        val api = Common.api
        var prods = ArrayList<Product>()
        for(product in productAdapter.products){
            prods.add(product)
        }

        var toAddBundle = bundleOf(
                "prods" to prods
            )

        it.findNavController().navigate(R.id.action_package_add_products_to_package_list_products, toAddBundle)

    }


    private fun getProducts(){
        val api = Common.api
        lateinit var resp : ArrayList<Product>
        api.getProducts(true, Session.user.Token, Session.user.KorisnickoIme).enqueue(object :
            Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                resp = response.body()!!.DATA!!

                when {
                    response.body()!!.STATUSMESSAGE=="OLD TOKEN" -> {
                        val intent = Intent(activity, LoginActivity::class.java)
                        Toast.makeText(context, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        activity?.finishAffinity()
                    }
                    response.body()!!.STATUSMESSAGE=="SUCCESS" -> {}
                    else -> Toast.makeText(context, response.body()!!.STATUSMESSAGE, Toast.LENGTH_LONG).show()
                }

                if (!resp.isNullOrEmpty()){
                }
                parentActivity = activity as ManagePackagesActivity
                productAdapter = ProductRecyclerAdapter(context)
                package_product_list.adapter = productAdapter
                var items = arguments!!.get("items") as ArrayList<Product>
                var finalItems = ArrayList<Product>()

                for (i:Product in resp){
                    var chk:Product?=null
                    for (j:Product in items){
                        if (j.Id==i.Id){
                            chk=j
                            break
                        }
                    }
                    if (chk!=null){
                        finalItems.add(chk)
                    }
                    else{
                        i.Kolicina=0.toString()
                        finalItems.add(i)
                    }
                }


                productAdapter.submitList(finalItems)
                package_product_list.layoutManager = LinearLayoutManager(context)
            }

        })

    }
}
