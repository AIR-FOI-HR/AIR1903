package com.example.pop

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Model.Product
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_manage_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ManageProductsActivity : AppCompatActivity() {
    internal lateinit var mService: IMyAPI
    private lateinit var product : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)
        mService = Common.api

        if(intent.hasExtra("product")) {
            layoutManageProductsButtonSubmit.text = getString(R.string.buttonSave)
            if(intent.getSerializableExtra("product") != null) {
                product = intent.getSerializableExtra("product") as Product
                layoutManageProductsInputName.setText(product.Naziv)
                layoutManageProductsInputValue.setText(product.Cijena.toString())
                layoutManageProductsInputDescription.setText(product.Opis)
                layoutManageProductsImage.setImageResource(R.drawable.prijava_bg)
            }
        }
        else {
            layoutManageProductsButtonSubmit.text = getString(R.string.buttonAdd)
        }

        layoutManageProductsButtonSubmit.setOnClickListener {
            if(intent.hasExtra("product")) {
                editProduct(
                    product.Id,
                    layoutManageProductsInputName.text.toString(),
                    layoutManageProductsInputDescription.text.toString(),
                    layoutManageProductsInputValue.text.toString(),
                    layoutManageProductsImage.toString()
                )
            }
            else {
                addProduct(
                    layoutManageProductsInputName.text.toString(),
                    layoutManageProductsInputDescription.text.toString(),
                    layoutManageProductsInputValue.text.toString(),
                    layoutManageProductsImage.toString()
                )
            }
        }

        layoutManageProductsImage.setOnClickListener {
            showImageOverlay()
        }

        layoutManageProductsImageOverlay.setOnClickListener {
            hideImageOverlay()
        }

        layoutManageProductsButtonDelete.setOnClickListener {
            //Slika se postavlja na defaultnu template sliku proizvoda
            product.Slika = "default.png"
            layoutManageProductsImage.setImageResource(R.drawable.ic_launcher_foreground) //Treba biti res na defaultnu sliku
            hideImageOverlay()
        }

        layoutManageProductsButtonEdit.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"),1)
        }
    }

    //U ovoj funkciji se slika učitava sa mobitela kao inputstream, od tu se treba spremiti/poslat na server
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val inputStream = contentResolver.openInputStream(data?.data!!)
            layoutManageProductsImage.setImageBitmap(BitmapFactory.decodeStream(inputStream))
            hideImageOverlay()
        }

    }

    private fun addProduct(Naziv: String, Opis: String, Cijena: String, Slika: String) {
        mService.addNewProduct(Session.user.Token, Naziv, Opis, Cijena, Slika).enqueue(object:
            Callback<NewProductResponse> {
            override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                Toast.makeText(this@ManageProductsActivity,t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                if (response!!.body()!!.STATUSMESSAGE=="SUCCESS"){
                    Toast.makeText(this@ManageProductsActivity,"Proizvod uspješno dodan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else if (response!!.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    var intent = Intent(this@ManageProductsActivity, LoginActivity::class.java)
                    Toast.makeText(this@ManageProductsActivity, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    Session.reset()
                    startActivity(intent)
                    finishAffinity()
                }
                else
                    Toast.makeText(this@ManageProductsActivity,response!!.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editProduct(Id: Int, Naziv: String, Opis: String, Cijena: String, Slika: String) {
        //Kod za editiranje proizvoda čija je referenca trenutno spremljena u product varijablu
        mService.editProduct(Session.user.Token, Id, Naziv, Opis, Cijena, Slika).enqueue(object:
            Callback<NewProductResponse> {
            override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                Toast.makeText(this@ManageProductsActivity,t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                if (response!!.body()!!.STATUSMESSAGE=="UPDATED"){
                    Toast.makeText(this@ManageProductsActivity,"Proizvod uspješno uređen", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else if (response!!.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    var intent = Intent(this@ManageProductsActivity, LoginActivity::class.java)
                    Toast.makeText(this@ManageProductsActivity, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    Session.reset()
                    startActivity(intent)
                    finishAffinity()
                }
                else
                    Toast.makeText(this@ManageProductsActivity,response!!.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun showImageOverlay() {
        layoutManageProductsImageOverlay.visibility = View.VISIBLE
        layoutManageProductsButtonEdit.visibility = View.VISIBLE
        layoutManageProductsButtonDelete.visibility = View.VISIBLE
    }

    private fun hideImageOverlay() {
        layoutManageProductsImageOverlay.visibility = View.INVISIBLE
        layoutManageProductsButtonEdit.visibility = View.INVISIBLE
        layoutManageProductsButtonDelete.visibility = View.INVISIBLE
    }
}