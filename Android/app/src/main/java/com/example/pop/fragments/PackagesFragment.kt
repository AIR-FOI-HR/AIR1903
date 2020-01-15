package com.example.pop.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pop.LoginActivity
import com.example.pop.ManagePackagesActivity
import com.example.pop.R
import com.example.pop.adapters.ItemRecyclerAdapter
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.PackageResponse
import kotlinx.android.synthetic.main.activity_show_products.*
import kotlinx.android.synthetic.main.fragment_packages_tab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackagesFragment : Fragment() {
    private lateinit var itemAdapter: ItemRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_packages_tab, container, false)
    }

    public fun getRecyclerAdapter() : ItemRecyclerAdapter{
        return package_list.adapter as ItemRecyclerAdapter
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            itemAdapter = ItemRecyclerAdapter(context)
            package_list.adapter = itemAdapter
            getPackages()

            btn_new_packet.setOnClickListener{addPackage()}
        }



        private fun getPackages(){
            val api = Common.api
            api.getAllPackages(Session.user.Token, true, Session.user.KorisnickoIme).enqueue(object :
                Callback<PackageResponse> {
                override fun onFailure(call: Call<PackageResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<PackageResponse>, response: Response<PackageResponse>) {
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

        private fun addPackage(){
            val intent= Intent(context, ManagePackagesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("previousActivity", 1)
            context?.startActivity(intent)
        }

}
