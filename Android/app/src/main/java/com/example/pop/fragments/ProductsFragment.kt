package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pop.*
import com.example.pop.adapters.ItemRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Item
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class ProductsFragment : Fragment() {
    private lateinit var itemAdapter: ItemRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAdapter = ItemRecyclerAdapter(context)
        product_list.adapter = itemAdapter
        getProducts()

        btn_new_product.setOnClickListener{addProduct()}
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

                if (resp != null) itemAdapter.submitList(resp)
            }
        })
    }

    private fun addProduct(){
        val intent= Intent(context, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 1)
        context?.startActivity(intent)
    }

   /* private fun createPackage() {
        val selectedProducts : List<Item> = itemAdapter.getSelectedItems()
        val intent = Intent(context, ManagePackagesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("productList", selectedProducts as Serializable)
        context?.startActivity(intent)
    }*/



}
