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
import com.example.pop.R
import com.example.pop.ShowItemsActivity
import com.example.webservice.Common.Common
import com.example.webservice.Model.Item
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.item_list.view.*
import retrofit2.Call
import retrofit2.Response


class ItemRecyclerAdapter(val context: Context?) : RecyclerView.Adapter<ItemViewHolder>(){

    private var items: ArrayList<Item> = ArrayList()
    private var selectedItems: ArrayList<Item> = ArrayList()
    lateinit var item: Item
    lateinit var activityContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(com.example.pop.R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        this.item = items[position]

        holder.bind(item)

        holder.itemView.setOnLongClickListener{expandItem(position)}
        holder.itemView.setOnClickListener{selectItem(position)}
        holder.itemView.img_edit_item.setOnClickListener{editItem()}
        holder.itemView.img_delete_item.setOnClickListener{deleteItem()}
        holder.itemView.img_return_item.setOnClickListener{expandItem(position)}
        activityContext=holder.itemView.img_delete_item.context

    }


    private fun expandItem(position : Int) : Boolean{
        val expanded = item.expanded
        item.expanded = !expanded
        notifyItemChanged(position)
        return true
    }

    private fun selectItem(position : Int) {
        val selected = item.selected

        if(!selected)
            selectedItems.add(item)
        else
            selectedItems.remove(item)

        item.selected = !selected
        notifyItemChanged(position)
    }

    private fun deleteItem() {
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

    private fun editItem(){
        val intent=Intent(this.context,ManageProductsActivity::class.java)
        intent.putExtra("item",item)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)

    }

    private fun deleteCall(popupWindow:PopupWindow){
        val mService:IMyAPI= Common.api
        mService.deleteProduct(com.example.pop_sajamv2.Session.user.Token, item.Id!!).enqueue(object : retrofit2.Callback<NewProductResponse> {
            override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                Toast.makeText(activityContext, t.message, Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                popupWindow.dismiss()
                if (response.body()!!.STATUSMESSAGE=="DELETED"){
                    val intent=Intent(activityContext,ShowItemsActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
                    activityContext.startActivity(intent)
                    (activityContext as Activity).overridePendingTransition(0,0)
                    (activityContext as Activity).finish()
                    (activityContext as Activity).overridePendingTransition(0,0)
                    Toast.makeText(activityContext,"Proizvod izbrisan", Toast.LENGTH_SHORT).show()
                }
                else if (response.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    Toast.makeText(activityContext, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    val intent = Intent(activityContext, LoginActivity::class.java)
                    com.example.pop_sajamv2.Session.reset()
                    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                    activityContext.startActivity(intent)
                    (activityContext as Activity).finish()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(data: List<Item>){
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun getSelectedItems() : List<Item>{
        return selectedItems
    }
}
