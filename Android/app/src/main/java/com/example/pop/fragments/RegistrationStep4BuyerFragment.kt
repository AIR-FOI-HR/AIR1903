package com.example.pop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.*
import com.example.pop.adapters.StoreClickListener
import com.example.pop.adapters.StoreRecyclerAdapter
import com.example.webservice.Common.Common
import com.example.webservice.Model.OneStoreResponse
import com.example.webservice.Model.Store
import com.example.webservice.Model.StoresResponse
import kotlinx.android.synthetic.main.fragment_registration_fourth_buyer.*
import kotlinx.android.synthetic.main.fragment_registration_fourth_buyer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationStep4BuyerFragment : Fragment(), View.OnClickListener, StoreClickListener {

    private lateinit var storeAdapter: StoreRecyclerAdapter
    private var stores: ArrayList<Store>? = ArrayList()
    var selectedStore: Int = 0
    var selectedPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=4
        val view:View = inflater.inflate(R.layout.fragment_registration_fourth_buyer, container, false)
        view.btn_next_step.setOnClickListener { onClick(view) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stores_list.layoutManager = LinearLayoutManager(context)
        storeAdapter = StoreRecyclerAdapter(context)
        storeAdapter.setClickListener(this)
        stores_list.adapter = storeAdapter
        getStores()
        storeAdapter.notifyDataSetChanged()
    }

    override fun onStoreClick(view: View, position: Int) {
        var store: Store?
        if(selectedPosition != -1){
            store = storeAdapter.getStore(selectedPosition)!!
            selectStore(store, selectedPosition)
        }

        store = storeAdapter.getStore(position)
        if (store != null) {
            selectStore(store, position)
        }
    }

    private fun selectStore(store: Store, position: Int) {
        val selected = store.selected

        if(!selected){
            selectedStore = store.Id_Trgovine!!
            selectedPosition = position
        }
        else
            selectedStore = 0
        store.selected = !selected

        storeAdapter.notifyItemChanged(position)
    }

    private fun getStores(){
        val mService = Common.api
        mService.getAllStores(com.example.pop_sajamv2.Session.user.Token, RegistrationData.KorisnickoIme, true).enqueue(object: Callback<StoresResponse>{
            override fun onFailure(call: Call<StoresResponse>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<StoresResponse>,
                response: Response<StoresResponse>
            ) {
                if(response.body()!!.STATUSMESSAGE=="SUCCESS") {
                    val stores: ArrayList<Store> = response.body()!!.DATA as ArrayList<Store>
                    storeAdapter.submitList(stores)
                }
                else
                    Toast.makeText(activity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                //println("DEBUG33 ++ "+(stores[0] as Store).Id_Trgovine)
            }
        })
    }

    override fun onClick(v: View?) {
        val mService = Common.api
        if (selectedStore==0){
            Toast.makeText(activity, getString(R.string.toast_store_not_set), Toast.LENGTH_SHORT).show()
            return
        }
        mService.assignStore(com.example.pop_sajamv2.Session.user.Token, RegistrationData.KorisnickoIme, true, selectedStore).enqueue(object: Callback<OneStoreResponse>{
            override fun onFailure(call: Call<OneStoreResponse>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OneStoreResponse>,
                response: Response<OneStoreResponse>
            ) {
                if(response.body()!!.STATUSMESSAGE=="STORE ASSIGNED") {
                    v!!.findNavController().navigate(R.id.action_registrationStep4Buyer_to_registrationStep5)
                }
                else
                    Toast.makeText(activity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
