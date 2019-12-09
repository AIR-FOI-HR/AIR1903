package com.example.pop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Model.Product
import com.example.webservice.Response.IMyAPI
import kotlinx.android.synthetic.main.activity_manage_products.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ManageProductsActivity : AppCompatActivity() {
    private lateinit var mService: IMyAPI
    private lateinit var product : Product
    private var imageFile : File? = null

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

        btn_add_products.setOnClickListener{changeQuantity(1)}
        btn_decrease_products.setOnClickListener{changeQuantity(-1)}

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
                    imageFile
                )
            }
        }

        btn_add_product_image.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data?.data != null){
            val uri:Uri = data.data!!
            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
                layoutManageProductsImage.setImageURI(uri)
            }
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)

            cursor.close()
            imageFile = File(picturePath)
        }
    }

    private fun changeQuantity(quantity: Int) {
        input_quantity.text = quantity.toString()
    }

    private fun addProduct(Naziv: String, Opis: String, Cijena: String, Slika: File?) {

        if(Slika!=null){
            var fileReqBody = RequestBody.create(MediaType.parse("image/*"), Slika)
            var part : MultipartBody.Part = MultipartBody.Part.createFormData("Slika", Slika.name, fileReqBody)

            var partToken = MultipartBody.Part.createFormData("Token", Session.user.Token)
            var partNaziv = MultipartBody.Part.createFormData("Naziv", Naziv)
            var partOpis = MultipartBody.Part.createFormData("Opis", Opis)
            var partCijena = MultipartBody.Part.createFormData("Cijena", Cijena)
            var partKorisnickoIme = MultipartBody.Part.createFormData("KorisnickoIme", Session.user.KorisnickoIme)

            mService.addNewProduct(partToken, partNaziv, partOpis, partCijena, part, partKorisnickoIme).enqueue(object:
            //mService.addNewProduct(part, description).enqueue(object:
                Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                    if (response.body()!!.STATUSMESSAGE=="SUCCESS"){
                        Toast.makeText(this@ManageProductsActivity,"Proizvod uspješno dodan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else if (response.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                        var intent = Intent(this@ManageProductsActivity, LoginActivity::class.java)
                        Toast.makeText(this@ManageProductsActivity, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    }
                    else
                        Toast.makeText(this@ManageProductsActivity,
                            response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                }
            })


        } else{
            println("DEBUG33: Slika ne postoji")
            mService.addNewProductNoImage(Session.user.Token, Naziv, Opis, Cijena).enqueue(object: Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                    if (response.body()!!.STATUSMESSAGE == "SUCCESS") {
                        Toast.makeText(
                            this@ManageProductsActivity,
                            "Proizvod uspješno dodan",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        var intent =
                            Intent(this@ManageProductsActivity, LoginActivity::class.java)
                        Toast.makeText(
                            this@ManageProductsActivity,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    } else
                        Toast.makeText(
                            this@ManageProductsActivity,
                            response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT
                        ).show()
                }
            })

        }
    }

    private fun editProduct(Id: Int, Naziv: String, Opis: String, Cijena: String, Slika: String) {
        //Kod za editiranje proizvoda čija je referenca trenutno spremljena u product varijablu
        mService.editProduct(Session.user.Token, Id, Naziv, Opis, Cijena, Slika).enqueue(object:
            Callback<NewProductResponse> {
            override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewProductResponse>, response: Response<NewProductResponse>) {
                if (response.body()!!.STATUSMESSAGE=="UPDATED"){
                    Toast.makeText(this@ManageProductsActivity,"Proizvod uspješno uređen", Toast.LENGTH_SHORT).show()
                    finish()
                    var intent=Intent(this@ManageProductsActivity,ShowProductsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    this@ManageProductsActivity.startActivity(intent)
                    (this@ManageProductsActivity as Activity).overridePendingTransition(0,0)
                    (this@ManageProductsActivity as Activity).finish()
                    (this@ManageProductsActivity as Activity).overridePendingTransition(0,0)
                    Toast.makeText(this@ManageProductsActivity,"Proizvod izbrisan", Toast.LENGTH_SHORT).show()
                }
                else if (response.body()!!.STATUSMESSAGE=="OLD TOKEN"){
                    var intent = Intent(this@ManageProductsActivity, LoginActivity::class.java)
                    Toast.makeText(this@ManageProductsActivity, "Sesija istekla, molimo prijavite se ponovno", Toast.LENGTH_LONG).show()
                    Session.reset()
                    startActivity(intent)
                    finishAffinity()
                }
                else
                    Toast.makeText(this@ManageProductsActivity,
                        response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
            }
        })

    }
}