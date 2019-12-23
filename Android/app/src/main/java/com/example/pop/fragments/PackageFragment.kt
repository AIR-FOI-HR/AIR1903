package com.example.pop.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pop.ManagePackagesActivity
import com.example.pop.ManageProductsActivity
import com.example.pop.R
import kotlinx.android.synthetic.main.fragment_package.view.*

class PackageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btn_add_package.setOnClickListener {
            addPackage()
            it.findNavController().navigate(R.id.action_packageFragment_to_packageProductsListing)
        }
    }

    private fun addPackage() {
        val intent= Intent(context, ManagePackagesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("previousActivity", 1)
        context?.startActivity(intent)
    }
}