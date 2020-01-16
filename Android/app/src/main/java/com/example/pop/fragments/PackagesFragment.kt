package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pop.LoginActivity
import com.example.pop.ManagePackagesActivity
import com.example.pop.R
import com.example.pop.adapters.ItemClickListener
import com.example.pop.adapters.ItemRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.PackageResponse
import com.example.webservice.Model.Product
import kotlinx.android.synthetic.main.fragment_packages_tab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackagesFragment : Fragment(), ItemClickListener {


    private lateinit var itemAdapter: ItemRecyclerAdapter
    private var packages: ArrayList<PackageClass>? = ArrayList()
    public var selectedPackages: ArrayList<PackageClass> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_packages_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPackages()
        package_list.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemRecyclerAdapter(context)
        itemAdapter.setClickListener(this)
        package_list.adapter = itemAdapter
        btn_new_packet.setOnClickListener { addPackage() }
        itemAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, position: Int) {
        val mPackage: PackageClass = packages!![position]
        Toast.makeText(context, mPackage.Naziv, Toast.LENGTH_SHORT).show()
        selectProduct(mPackage, position)
        selectedPackages.forEach{
            Log.e("Dodani proizvodi", it.Naziv)
        }
    }

    override fun onItemLongClick(view: View?, position: Int) {
        val mPackage: PackageClass = packages!![position]
        mPackage.expanded = mPackage.expanded.not()
        Log.e("EXPAND", mPackage.expanded.toString())
        itemAdapter.notifyItemChanged(position)
    }

    override fun onItemDeleteClick(view: View?, position: Int) {
        Toast.makeText(context, "DELETE" + (packages!![position].Naziv), Toast.LENGTH_SHORT).show()
    }

    override fun onItemEditClick(view: View?, position: Int) {
        Toast.makeText(context, "EDIT" + (packages!![position].Naziv), Toast.LENGTH_SHORT).show()
    }

    private fun selectProduct(selectedPackage: PackageClass, position: Int) {
        val selected = selectedPackage.selected

        if(!selected)
            selectedPackages.add(selectedPackage)
        else
            selectedPackages.remove(selectedPackage)

        selectedPackage.selected = !selected

        itemAdapter.notifyItemChanged(position)
    }

    private fun getPackages() {
        val api = Common.api
        api.getAllPackages(Session.user.Token, true, Session.user.KorisnickoIme).enqueue(object :
            Callback<PackageResponse> {
            override fun onFailure(call: Call<PackageResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<PackageResponse>,
                response: Response<PackageResponse>
            ) {
                packages = response.body()!!.DATA
                when {
                    response.body()!!.STATUSMESSAGE == "OLD TOKEN" -> {
                        val intent = Intent(activity, LoginActivity::class.java)
                        Toast.makeText(
                            context,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        activity?.finishAffinity()
                    }
                    response.body()!!.STATUSMESSAGE == "OK" -> {
                    }
                    else -> Toast.makeText(
                        context,
                        response.body()!!.STATUSMESSAGE,
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (packages != null) itemAdapter.submitList(packages!!)
            }
        })
    }

    private fun addPackage() {
        val intent = Intent(context, ManagePackagesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 1)
        context?.startActivity(intent)
    }

}
