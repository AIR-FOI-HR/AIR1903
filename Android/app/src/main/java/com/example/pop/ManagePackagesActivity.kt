package com.example.pop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewPackageResponse
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Response.IMyAPI
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manage_products.*
import kotlinx.android.synthetic.main.activity_manage_products.image_item_picture
import kotlinx.android.synthetic.main.activity_manage_products.input_quantity
import kotlinx.android.synthetic.main.activity_manage_products.layoutManageProductsInputValue
import kotlinx.android.synthetic.main.activity_manage_products.manage_products
import kotlinx.android.synthetic.main.dialog_add_image.view.*
import kotlinx.android.synthetic.main.fragment_package.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class ManagePackagesActivity : AppCompatActivity() {

    private lateinit var mService: IMyAPI
    private lateinit var package: Package
    private var imageFile: File? = null
    private lateinit var image: File
    private lateinit var packageUrl: String
    lateinit var previousActivity: Class<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = File(applicationContext!!.cacheDir.path + "/temp.webp")
        setContentView(R.layout.activity_manage_packages)
        mService = Common.api

        if (intent.getIntExtra("previousActivity", 1) == 1)
            previousActivity = ShowItemsActivity::class.java
        else if (intent.getIntExtra("previousActivity", 1) == 2)
            previousActivity = MainMenu::class.java

        if (intent.hasExtra("item")) {
            if (intent.getSerializableExtra("item") != null) {
                package = intent.getSerializableExtra("item") as Package
                packageUrl = package.Slika!!
                layoutManagePacketsInputName.setText(package.Naziv)
                layoutManageProductsInputValue.setText(package.Cijena)
                layoutManagePacketsInputDescription.setText(package.Opis)
                image_item_picture.setImageResource(R.drawable.prijava_bg)
                input_quantity.setText(package.Kolicina)
            }
            Picasso.get().load(packageUrl).into(image_item_picture)

        }

        btn_add_package.setOnClickListener { changeQuantity(1) }
        btn_decrease_packages.setOnClickListener { changeQuantity(-1) }
        image_item_picture.setOnClickListener { addImage() }


        layoutManageProductsButtonSubmit.setOnClickListener {
            if (intent.hasExtra("item")) {
                updatePackageWithImage(
                    package.Id!!,
                    layoutManagePacketsInputName.text.toString(),
                    layoutManagePacketsInputDescription.text.toString(),
                    layoutManageProductsInputValue.text.toString(),
                    input_quantity.text.toString().toInt(),
                    imageFile
                )
            } else {
                addPackage(
                    layoutManagePacketsInputName.text.toString(),
                    layoutManagePacketsInputDescription.text.toString(),
                    layoutManageProductsInputValue.text.toString(),
                    input_quantity.text.toString().toInt(),
                    imageFile
                )
            }
        }


    }


    }

}