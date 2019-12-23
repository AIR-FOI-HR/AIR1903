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

    private fun addImage() {
        val layoutInflater: LayoutInflater = this.applicationContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dialogView = layoutInflater.run { inflate(R.layout.dialog_add_image, null) }
        val dialogWindow = PopupWindow(
            dialogView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogView.btn_close_image_dialog.setOnClickListener { dialogWindow.dismiss() }

        dialogView.btn_gallery.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is <  Marshmallow
                pickImageFromGallery()
            }
            dialogWindow.dismiss()
        }

        dialogView.btn_camera.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) { // no camera permission
                    val permissions = arrayOf(Manifest.permission.CAMERA)
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{ // has camera permission
                    getImageFromCamera()
                }
            }
            else{ // <Marshmallow
                getImageFromCamera()
            }
        }

        dialogWindow.showAtLocation(manage_products, Gravity.CENTER, 0, 0)
        dialogWindow.dimBehind()
    }

    private fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.7f
        wm.updateViewLayout(container, p)
    }

    private fun pickImageFromGallery() {
        //Intent to pick image

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    private fun getImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", image)
        )
        startActivityForResult(cameraIntent, CAMERA_CAPTURE)
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
        //camera code
        private const val CAMERA_CAPTURE = 1002
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_CAPTURE) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                val imageBitmap = BitmapFactory.decodeFile(image.path)
                image = compressImage(imageBitmap)

                imageFile = File(image.path)
                Picasso.get()
                    .load(
                        FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            image
                        )
                    )
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image_item_picture)
                packageUrl = ""
            }
        }

        if (data?.data != null) {
            val uri: Uri = data.data!!
            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                image_item_picture.setImageURI(uri)
            }


            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)

            cursor.close()
            val imageBitmap = BitmapFactory.decodeFile(picturePath)
            image = compressImage(imageBitmap)
            imageFile = File(image.path)

            packageUrl = ""
        }
    }

    private fun compressImage(originalImage: Bitmap): File {
        var bitmap = originalImage
        var height = 0
        var width = 0
        if (bitmap.width > 2000 || bitmap.height > 2000) {
            if (bitmap.width >= bitmap.height) {
                val ratio: Float = 1 / (bitmap.width.toFloat() / 2000)
                width = 2000
                height = (bitmap.height * ratio).toInt()
            } else {
                val ratio: Float = 1 / (bitmap.height.toFloat() / 2000)
                height = 2000
                width = (bitmap.width * ratio).toInt()
            }

            bitmap = bitmap.scale(width, height)
        }

        val stream = FileOutputStream(image)
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, stream)
        return image
    }

    private fun changeQuantity(value: Int) {
        var newValue = input_quantity.text.toString().toIntOrNull()
        if (newValue != null) {
            newValue += value
            if (newValue < 0) newValue = 0
            input_quantity.setText(newValue.toString())
        } else input_quantity.setText("0")
    }

   

    }

}