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
import android.text.InputFilter
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import com.example.core.BaseActivity
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewProductResponse
import com.example.webservice.Model.Product
import com.example.webservice.Response.IMyAPI
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manage_products.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlinx.android.synthetic.main.dialog_add_image.view.*


class ManageProductsActivity : BaseActivity() {
    private lateinit var mService: IMyAPI
    private lateinit var product: Product
    private var imageFile: File? = null
    private lateinit var image: File
    private lateinit var productUrl: String
    lateinit var previousActivity: Class<*>
    val pattern = Regex(pattern = "^(([1-9][0-9]{0,4})|(0))(\\.[0-9]{1,2})?\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = File(applicationContext!!.cacheDir.path + "/temp.webp")
        setContentView(R.layout.activity_manage_products)
        mService = Common.api

        if (intent.getIntExtra("previousActivity", 1) == 1)
            previousActivity = ShowItemsActivity::class.java
        else if (intent.getIntExtra("previousActivity", 1) == 2)
            previousActivity = MainMenuSeller::class.java

        if (intent.hasExtra("item")) {
            if (intent.getSerializableExtra("item") != null) {
                product = intent.getSerializableExtra("item") as Product
                productUrl = product.Slika!!
                input_product_name.setText(product.Naziv)
                input_product_price.setText(product.Cijena)
                input_product_description.setText(product.Opis)
                image_product_picture.setImageResource(R.drawable.prijava_bg)
                input_product_quantity.setText(product.Kolicina)
            }
            Picasso.get().load(productUrl).into(image_product_picture)

        }


        input_product_price.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "9999"))
        input_product_quantity.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "9999"))

      /*  input_product_price.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if(!pattern.matches(input_product_price.toString()))
                    input_product_price.setText("0") // LOOPa vjecno
            }
        })*/




        image_product_picture.setOnClickListener { addImage() }
        btn_finish_product.setOnClickListener {
            if (intent.hasExtra("item")) {
                editProduct(
                    product.Id!!,
                    input_product_name.text.toString(),
                    input_product_description.text.toString(),
                    input_product_price.text.toString(),
                    input_product_quantity.text.toString().toInt(),
                    imageFile
                )
            } else {
                addProduct(
                    input_product_name.text.toString(),
                    input_product_description.text.toString(),
                    input_product_price.text.toString(),
                    input_product_quantity.text.toString().toInt(),
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
            dialogWindow.dismiss()
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
                    .into(image_product_picture)
                productUrl = ""
            }
        }

        if (data?.data != null) {
            val uri: Uri = data.data!!
            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                image_product_picture.setImageURI(uri)
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

            productUrl = ""
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
        var newValue = input_product_quantity.text.toString().toIntOrNull()
        if (newValue != null) {
            newValue += value
            if (newValue < 0) newValue = 0
            input_product_quantity.setText(newValue.toString())
        } else input_product_quantity.setText("0")
    }

    private fun addProduct(
        Naziv: String,
        Opis: String,
        Cijena: String,
        Kolicina: Int,
        Slika: File?
    ) {

        if (Slika != null) {
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), Slika)
            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("Slika", Slika.name, fileReqBody)

            val partToken = MultipartBody.Part.createFormData("Token", Session.user.Token)
            val partNaziv = MultipartBody.Part.createFormData("Naziv", Naziv)
            val partOpis = MultipartBody.Part.createFormData("Opis", Opis)
            val partCijena = MultipartBody.Part.createFormData("Cijena", Cijena)
            val partKolicina = MultipartBody.Part.createFormData("Kolicina", Kolicina.toString())
            val partKorisnickoIme =
                MultipartBody.Part.createFormData("KorisnickoIme", Session.user.KorisnickoIme)

            mService.addNewProduct(
                partToken,
                partNaziv,
                partOpis,
                partCijena,
                partKolicina,
                part,
                partKorisnickoIme
            ).enqueue(object :
                Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewProductResponse>,
                    response: Response<NewProductResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "SUCCESS") {
                        Toast.makeText(this@ManageProductsActivity,getString(R.string.toast_product_added), Toast.LENGTH_SHORT ).show()
                        val intent = Intent(this@ManageProductsActivity, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        this@ManageProductsActivity.startActivity(intent)
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                        (this@ManageProductsActivity as Activity).finish()
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent = Intent(this@ManageProductsActivity, LoginActivity::class.java)
                        Toast.makeText(this@ManageProductsActivity, getString(R.string.toast_session_expired),  Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    } else
                        Toast.makeText(this@ManageProductsActivity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                }
            })


        } else {
            mService.addNewProductNoImage(
                Session.user.Token,
                Naziv,
                Opis,
                Cijena,
                Kolicina,
                Session.user.KorisnickoIme
            ).enqueue(object : Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<NewProductResponse>,
                    response: Response<NewProductResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "SUCCESS") {
                        Toast.makeText(this@ManageProductsActivity, getString(R.string.toast_product_added), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ManageProductsActivity, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        this@ManageProductsActivity.startActivity(intent)
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                        (this@ManageProductsActivity as Activity).finish()
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent =
                            Intent(this@ManageProductsActivity, LoginActivity::class.java)
                        Toast.makeText(this@ManageProductsActivity,getString(R.string.toast_session_expired) ,Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    } else
                        Toast.makeText(this@ManageProductsActivity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun editProduct(
        Id: Int,
        Naziv: String,
        Opis: String,
        Cijena: String,
        Kolicina: Int,
        Slika: File?
    ) {
        //Kod za editiranje proizvoda čija je referenca trenutno spremljena u item varijablu
        if (productUrl == "") {
            lateinit var part: MultipartBody.Part
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), Slika)
            part = MultipartBody.Part.createFormData("Slika", Slika?.name, fileReqBody)


            val partEdit = MultipartBody.Part.createFormData("Edit", "true")
            val partToken = MultipartBody.Part.createFormData("Token", Session.user.Token)
            val partId = MultipartBody.Part.createFormData("Id", Id.toString())
            val partNaziv = MultipartBody.Part.createFormData("Naziv", Naziv)
            val partOpis = MultipartBody.Part.createFormData("Opis", Opis)
            val partCijena = MultipartBody.Part.createFormData("Cijena", Cijena)
            val partKolicina = MultipartBody.Part.createFormData("Kolicina", Kolicina.toString())
            val partKorisnickoIme =
                MultipartBody.Part.createFormData("KorisnickoIme", Session.user.KorisnickoIme)

            mService.editProduct(
                partEdit,
                partToken,
                partId,
                partNaziv,
                partOpis,
                partCijena,
                partKolicina,
                part,
                partKorisnickoIme
            ).enqueue(object :
                Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewProductResponse>,
                    response: Response<NewProductResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "UPDATED") {
                        Toast.makeText(this@ManageProductsActivity,getString(R.string.toast_product_edited), Toast.LENGTH_SHORT).show()
                        finish()
                        val intent = Intent(this@ManageProductsActivity, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        this@ManageProductsActivity.startActivity(intent)
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                        (this@ManageProductsActivity as Activity).finish()
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent = Intent(this@ManageProductsActivity, LoginActivity::class.java)
                        Toast.makeText(this@ManageProductsActivity,getString(R.string.toast_session_expired) , Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    } else
                        Toast.makeText(this@ManageProductsActivity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            mService.editProductNoImage(
                true,
                Session.user.Token,
                Id,
                Naziv,
                Opis,
                Cijena,
                Kolicina,
                productUrl,
                Session.user.KorisnickoIme
            ).enqueue(object : Callback<NewProductResponse> {
                override fun onFailure(call: Call<NewProductResponse>, t: Throwable) {
                    Toast.makeText(this@ManageProductsActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewProductResponse>,
                    response: Response<NewProductResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "UPDATED") {
                        Toast.makeText(this@ManageProductsActivity,getString(R.string.toast_product_edited), Toast.LENGTH_SHORT).show()
                        finish()
                        val intent = Intent(this@ManageProductsActivity, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        this@ManageProductsActivity.startActivity(intent)
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                        (this@ManageProductsActivity as Activity).finish()
                        (this@ManageProductsActivity as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent =
                            Intent(this@ManageProductsActivity, LoginActivity::class.java)
                        Toast.makeText(this@ManageProductsActivity,getString(R.string.toast_session_expired), Toast.LENGTH_LONG).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity()
                    } else
                        Toast.makeText(this@ManageProductsActivity, response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}