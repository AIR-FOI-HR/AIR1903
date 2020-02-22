package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.ManageProductsActivity
import com.example.pop.R
import com.example.pop.adapters.ItemClickListener
import com.example.pop.adapters.ItemRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Product
import com.example.webservice.Model.ProductResponse
import kotlinx.android.synthetic.main.activity_show_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsFragment : Fragment(), ItemClickListener {

    private lateinit var itemAdapter: ItemRecyclerAdapter
    private var products: ArrayList<Product>? = ArrayList()
    var selectedProducts: ArrayList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProducts()
        product_list.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemRecyclerAdapter(context)
        itemAdapter.setClickListener(this)

        product_list.adapter = itemAdapter
        itemAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, position: Int) {
        val product: Product = products!![position]

        //Toast.makeText(context, product.Naziv, Toast.LENGTH_SHORT).show()
        selectProduct(product,position)
    }

    override fun onItemLongClick(view: View?, position: Int) {
        val product: Product = products!![position]
        product.expanded = product.expanded.not()
        itemAdapter.notifyItemChanged(position)
    }

    override fun onItemDeleteClick(view: View?, position: Int) {
        //Toast.makeText(context, "DELETE" + (products!![position].Naziv), Toast.LENGTH_SHORT).show()
    }

    override fun onItemEditClick(view: View?, position: Int) {
        //Toast.makeText(context, "EDIT" + (products!![position].Naziv), Toast.LENGTH_SHORT).show()
    }

    private fun selectProduct(selectedProduct: Product, position: Int) {
        val selected = selectedProduct.selected

        if(!selected)
            selectedProducts.add(selectedProduct)
        else
            selectedProducts.remove(selectedProduct)

        selectedProduct.selected = !selected

        itemAdapter.notifyItemChanged(position)
    }

    private fun getProducts(){
        val api = Common.api
        api.getProducts(true, Session.user.Token, Session.user.KorisnickoIme).enqueue(object :
            Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.body()!!.STATUSMESSAGE=="USER NOT IN STORE"){
                    Toast.makeText(context, getString(R.string.toast_user_not_in_store), Toast.LENGTH_LONG).show()
                    return
                }
                else if (response.body()!!.STATUSMESSAGE=="OK, NO PRODUCTS"){
                    Toast.makeText(context, getString(R.string.toast_store_no_products), Toast.LENGTH_LONG).show()
                    return
                }
                products = response.body()!!.DATA
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
                if(products != null){
                    itemAdapter.submitList(products!!)
                }
            }
        })
    }

    fun addProduct(){
        val intent= Intent(context, ManageProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 1)
        context?.startActivity(intent)
    }

}
