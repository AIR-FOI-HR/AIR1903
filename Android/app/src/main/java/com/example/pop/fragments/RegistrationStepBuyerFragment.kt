package com.example.pop.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.*
import com.example.pop.adapters.StoreClickListener
import com.example.pop.adapters.StoreRecyclerAdapter
import com.example.webservice.Model.Store
import kotlinx.android.synthetic.main.fragment_registration_fourth_buyer.*
import kotlinx.android.synthetic.main.fragment_registration_fourth_buyer.view.*

class RegistrationStepBuyerFragment : Fragment(), View.OnClickListener, StoreClickListener {

    private lateinit var storeAdapter: StoreRecyclerAdapter
    private var stores: ArrayList<Store>? = ArrayList()
    var selectedStore : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (this.activity as RegistrationActivity).currentFragment=4
        val view:View = inflater.inflate(R.layout.fragment_registration_fourth_buyer, container, false)
        view.btnNextStep.setOnClickListener { onClick(view) }
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
        val store: Store? = storeAdapter.getStore(position)
        if (store != null) {
            Log.e("ODABRANA TRGOVINA: ", store.Naziv.toString())
            selectStore(store, position)
        }
    }

    private fun selectStore(store: Store, position: Int) {
        val selected = store.selected

        if(!selected)
            selectedStore = store.Naziv!!
        else
            selectedStore = ""
        store.selected = !selected

        storeAdapter.notifyItemChanged(position)
    }

    private fun getStores(){
        val stores: ArrayList<Store> = arrayListOf(
            Store(1, "Trgovina 1"),
            Store(2, "Trgovina 2"),
            Store(3, "Trgovina 3"),
            Store(4, "Trgovina 4"),
            Store(5, "Trgovina 5"))
        storeAdapter.submitList(stores)
    }

    override fun onClick(v: View?) {
        RegistrationData.Trgovina = selectedStore
        v!!.findNavController().navigate(R.id.action_registrationStep4Buyer_to_registrationStep5)
    }
}
