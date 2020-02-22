package com.example.pop.adapters

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.se.omapi.Session
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pop.*
import com.example.webservice.Model.*
import android.view.LayoutInflater
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.webservice.Common.Common
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.item_list.view.*
import retrofit2.Call
import retrofit2.Response

interface ItemClickListener {
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View?, position: Int)
    fun onItemDeleteClick(view: View?, position: Int)
    fun onItemEditClick(view: View?, position: Int)
}


class ItemRecyclerAdapter(
    val context: Context?
) : RecyclerView.Adapter<ItemViewHolder>(){

    private var items: ArrayList<Item> = ArrayList()
    var mClickListener: ItemClickListener? = null
    private var mInflater: LayoutInflater? = null
    lateinit var item: Item
    lateinit var activityContext: Context


    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int) : ItemViewHolder {
        val view = mInflater!!.inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = items[position]
        holder.bind(item, mClickListener)

        holder.itemView.img_edit_item.setOnClickListener{editItem(position)}
        holder.itemView.img_delete_item.setOnClickListener{deleteItem(position)}
        activityContext = holder.itemView.img_delete_item.context

    }

    override fun getItemCount(): Int {
        return items.size
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }


    fun getItem(position: Int): Item? {
        return items[position]
    }



    private fun deleteItem(position: Int) {
        val layoutInflater:LayoutInflater = context
            ?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView = layoutInflater.inflate(R.layout.dialog_delete_confirmation, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val btnDismiss = popupView.findViewById(R.id.button_popup_no) as Button
        val btnAccept = popupView.findViewById(R.id.button_popup_yes) as Button

        val  anchor:View = (activityContext as Activity).findViewById(R.id.item_card) as CardView
        btnDismiss.setOnClickListener {run { popupWindow.dismiss()} }
        btnAccept.setOnClickListener { deleteCall(popupWindow, position) }
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

    private fun editItem(position: Int) {
        lateinit var intent: Intent
        item = items[position]
        if (item is Product)
            intent=Intent(this.context,ManageProductsActivity::class.java)
        else if (item is PackageClass)
            intent=Intent(this.context,ManagePackagesActivity::class.java)
        intent.putExtra("item",item)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)

    }

    private fun deleteCall(popupWindow: PopupWindow, position: Int){
        val mService: IMyAPI = Common.api
        item = items[position]
        if (item is Product)
            mService.deleteProduct(com.example.pop_sajamv2.Session.user.Token, com.example.pop_sajamv2.Session.user.KorisnickoIme, item.Id!!).enqueue(object : retrofit2.Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(activityContext, t.message, Toast.LENGTH_SHORT).show()
                    popupWindow.dismiss()
                }

                override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                    popupWindow.dismiss()
                    if (response.body()!!.STATUSMESSAGE=="DELETED"){
                        val intent=Intent(activityContext, ShowItemsActivity::class.java)
                        intent.flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
                        activityContext.startActivity(intent)
                        (activityContext as Activity).overridePendingTransition(0,0)
                        (activityContext as Activity).finishAffinity()
                        (activityContext as Activity).overridePendingTransition(0,0)
                        Toast.makeText(activityContext,activityContext.getString(R.string.toast_product_deleted), Toast.LENGTH_SHORT).show()
                    }
                    else if (response.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                        Toast.makeText(activityContext, activityContext.getString(R.string.toast_session_expired), Toast.LENGTH_LONG).show()
                        val intent = Intent(activityContext, LoginActivity::class.java)
                        com.example.pop_sajamv2.Session.reset()
                        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                        activityContext.startActivity(intent)
                        (activityContext as Activity).finish()
                    }
                }
            })
        else if (item is PackageClass)
            mService.deletePackage(com.example.pop_sajamv2.Session.user.Token, com.example.pop_sajamv2.Session.user.KorisnickoIme,true, item.Id!!.toString()).enqueue(object : retrofit2.Callback<NewPackageResponse> {
                override fun onFailure(call: Call<NewPackageResponse>, t: Throwable) {
                    Toast.makeText(activityContext, t.message, Toast.LENGTH_SHORT).show()
                    popupWindow.dismiss()
                }

                override fun onResponse(call: Call<NewPackageResponse>, response: Response<NewPackageResponse>) {
                    popupWindow.dismiss()
                    if (response.body()!!.STATUSMESSAGE=="DELETED"){
                        val intent=Intent(activityContext,ShowItemsActivity::class.java)
                        intent.flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
                        activityContext.startActivity(intent)
                        (activityContext as Activity).overridePendingTransition(0,0)
                        (activityContext as Activity).finish()
                        (activityContext as Activity).overridePendingTransition(0,0)
                        Toast.makeText(activityContext,activityContext.getString(R.string.toast_product_deleted), Toast.LENGTH_SHORT).show()
                    }
                    else if (response.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                        Toast.makeText(activityContext, activityContext.getString(R.string.toast_session_expired), Toast.LENGTH_LONG).show()
                        val intent = Intent(activityContext, LoginActivity::class.java)
                        com.example.pop_sajamv2.Session.reset()
                        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                        activityContext.startActivity(intent)
                        (activityContext as Activity).finish()
                    }
                }
            })
    }



    fun submitList(data: List<Item>){
        items.addAll(data)
        notifyDataSetChanged()
    }
}

