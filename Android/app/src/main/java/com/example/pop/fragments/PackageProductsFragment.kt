package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.*
import com.example.pop.adapters.ProductRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Product
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.fragment_package_products.*
import kotlinx.android.synthetic.main.fragment_package_products.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class PackageProductsFragment : Fragment() {

    private lateinit var productAdapter: ProductRecyclerAdapter
    var parentActivity: ManagePackagesActivity = ManagePackagesActivity()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_package_products, container, false)
        view.package_product_list.setOnTouchListener { v : View, event : MotionEvent ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                packageTouchX = event.x
            }
            if (event.action == MotionEvent.ACTION_UP) {
                if(abs(packageTouchX - event.x) > SWIPE_THRESHOLD) {
                    if(packageTouchX - event.x < 0) addProducts(view)
                }
            }
            true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_products.setOnClickListener {
            addProducts(it)
        }

        getProducts()

    }

    private fun addProducts(it:View){
        val api = Common.api
        var prods = ArrayList<Product>()
        for(product in productAdapter.products){
            if (product.Kolicina!=0.toString())
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
                resp = response.body()!!.DATA!! as ArrayList<Product>

                when {
                    response.body()!!.STATUSMESSAGE=="OLD TOKEN" -> {
                        val intent = Intent(activity, LoginActivity::class.java)
                        Toast.makeText(context, getString(R.string.toast_session_expired), Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        activity?.finishAffinity()
                    }
                    response.body()!!.STATUSMESSAGE=="OK" -> {}
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
                    var chk=0
                    for (j:Product in items){
                        if (j.Id==i.Id){
                            i.Kolicina=j.Kolicina
                            finalItems.add(i)
                            chk=1
                            break
                        }
                    }
                    if (chk==0){
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
