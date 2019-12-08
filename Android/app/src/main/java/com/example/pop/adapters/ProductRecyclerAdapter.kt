package com.example.pop.adapters

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.*
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.LoginActivity
import com.example.pop.ManageProductsActivity
import com.example.pop.ShowProductsActivity
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Model.Product
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.product_list_item.view.*
import retrofit2.Call
import retrofit2.Response


class ProductRecyclerAdapter (val context: Context) : RecyclerView.Adapter<ProductViewHolder>(){

    private var products: ArrayList<Product> = ArrayList()
    private var selectedProducts: ArrayList<Product> = ArrayList()
    lateinit var product:Product
    lateinit var activityContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(com.example.pop.R.layout.product_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        this.product = products[position]

        holder.bind(product)

        holder.itemView.setOnLongClickListener{expandProduct(position)}
        holder.itemView.setOnClickListener{selectProduct(position)}
        holder.itemView.img_edit_product.setOnClickListener{editProduct()}
        holder.itemView.img_delete_product.setOnClickListener{deleteProduct()}
        holder.itemView.img_return_card.setOnClickListener{expandProduct(position)}
        activityContext=holder.itemView.img_delete_product.context

    }


    private fun expandProduct(position : Int) : Boolean{
        val expanded = product.isExpanded
        product.isExpanded = !expanded
        notifyItemChanged(position)
        return true
    }

    private fun selectProduct(position : Int) {
        val selected = product.isSelected

        if(!selected)
            selectedProducts.add(product)
        else
            selectedProducts.remove(product)

        product.isSelected = !selected
        notifyItemChanged(position)
    }

    private fun deleteProduct() {
        val layoutInflater:LayoutInflater = context
            .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView = layoutInflater.inflate(com.example.pop.R.layout.popup_delete_confirmation, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val btnDismiss = popupView.findViewById(com.example.pop.R.id.button_popup_no) as Button
        val btnAccept = popupView.findViewById(com.example.pop.R.id.button_popup_yes) as Button

        var  anchor:View = (activityContext as Activity).findViewById(com.example.pop.R.id.product_card) as CardView
        btnDismiss.setOnClickListener {run { popupWindow.dismiss()} }
        btnAccept.setOnClickListener { deleteCall(popupWindow) }
        popupWindow.showAtLocation(anchor, Gravity.CENTER, 0,0)
        popupWindow.dimBehind()
    }

    private fun PopupWindow.dimBehind(){
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.7f
        wm.updateViewLayout(container, p)
    }

    private fun editProduct(){
        val intent=Intent(this.context,ManageProductsActivity::class.java)
        intent.putExtra("product",product)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }

    fun deleteCall(popupWindow:PopupWindow){
        var mService:IMyAPI= Common.api
        mService.deleteProduct(com.example.pop_sajamv2.Session.user.Token, product.Id).enqueue(object : retrofit2.Callback<NewProductResponse> {
            override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                Toast.makeText(activityContext, t!!.message, Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                popupWindow.dismiss()
                if (response!!.body()!!.STATUSMESSAGE=="DELETED"){
                    var intent=Intent(activityContext,ShowProductsActivity::class.java)
                    intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
                    activityContext.startActivity(intent)
                    (activityContext as Activity).overridePendingTransition(0,0)
                    (activityContext as Activity).finish()
                    (activityContext as Activity).overridePendingTransition(0,0)
                    Toast.makeText(activityContext,"Proizvod izbrisan", Toast.LENGTH_SHORT).show()
                }
                else if (response!!.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    Toast.makeText(activityContext, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    var intent = Intent(activityContext, LoginActivity::class.java)
                    com.example.pop_sajamv2.Session.reset()
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                    activityContext.startActivity(intent)
                    (activityContext as Activity).finish()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun submitList(data: List<Product>){
        products.addAll(data)
        notifyDataSetChanged()
    }


}
