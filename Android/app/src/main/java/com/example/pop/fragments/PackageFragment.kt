package com.example.pop.fragments

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
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pop.*
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.NewPackageResponse
import com.example.webservice.Model.PackageClass
import com.example.webservice.Response.IMyAPI
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manage_products.*
import kotlinx.android.synthetic.main.dialog_add_image.view.*
import kotlinx.android.synthetic.main.fragment_package.*
import kotlinx.android.synthetic.main.fragment_package.image_item_picture
//import kotlinx.android.synthetic.main.fragment_package.package_quantity
import kotlinx.android.synthetic.main.fragment_package.layoutManagePacketsInputValue
import kotlinx.android.synthetic.main.fragment_package.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class PackageFragment : Fragment() {

    private lateinit var mService: IMyAPI
    private lateinit var packageClass: PackageClass
    private var imageFile: File? = null
    private lateinit var image: File
    private lateinit var packageUrl: String
    lateinit var previousActivity: Class<*>
    private lateinit var intent: Intent
    private lateinit var appContext:Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext=activity as Context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            return inflater.inflate(R.layout.fragment_package, container, false)
    }

    override fun onStart() {
        super.onStart()
        image = File(activity!!.applicationContext!!.cacheDir.path + "/temp.webp")
        ////setContentView(R.layout.activity_manage_packages)
        intent = activity!!.intent
        mService = Common.api

        if (intent.getIntExtra("previousActivity", 1) == 1)
            previousActivity = ShowItemsActivity::class.java
        else if (intent.getIntExtra("previousActivity", 1) == 2)
            previousActivity = MainMenu::class.java

        if (intent.hasExtra("item")) {
            if (intent.getSerializableExtra("item") != null) {
                packageClass = intent.getSerializableExtra("item") as PackageClass
                packageUrl = packageClass.Slika!!
                layoutManagePacketsInputName.setText(packageClass.Naziv)
                layoutManagePacketsInputValue.setText(packageClass.Popust)
                layoutManagePacketsInputDescription.setText(packageClass.Opis)
                image_item_picture.setImageResource(R.drawable.prijava_bg)
                //package_quantity.setText(packageClass.Kolicina)
            }
            Picasso.get().load(packageUrl).into(image_item_picture)

        }

        //btn_add_packages.setOnClickListener { changeQuantity(1) }
        //btn_decrease_packages.setOnClickListener { changeQuantity(-1) }
        image_item_picture.setOnClickListener { addImage() }


        btn_add_package.setOnClickListener {
            if (intent.hasExtra("item")) {
                updatePackageWithImage(
                    packageClass.Id!!,
                    layoutManagePacketsInputName.text.toString(),
                    layoutManagePacketsInputDescription.text.toString(),
                    layoutManagePacketsInputValue.text.toString(),
                    //package_quantity.text.toString().toInt(),
                    1,
                    imageFile
                )
            } else {
                addPackage(
                    layoutManagePacketsInputName.text.toString(),
                    layoutManagePacketsInputDescription.text.toString(),
                    layoutManagePacketsInputValue.text.toString(),
                    //package_quantity.text.toString().toInt(),
                    1,
                    imageFile
                )
            }
        }
    }

    private fun addImage() {
        val layoutInflater: LayoutInflater = appContext
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
                if (checkSelfPermission(appContext,Manifest.permission.READ_EXTERNAL_STORAGE) ==
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
                if (checkSelfPermission(appContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) { // no camera permission
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

        dialogWindow.showAtLocation(manage_packets, Gravity.CENTER, 0, 0)
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

    /*private fun changeQuantity(value: Int) {
        var newValue = package_quantity.text.toString().toIntOrNull()
        if (newValue != null) {
            newValue += value
            if (newValue < 0) newValue = 0
            package_quantity.setText(newValue.toString())
        } else package_quantity.setText("0")
    }*/

    private fun getImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(appContext, BuildConfig.APPLICATION_ID + ".provider", image)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btn_add_package.setOnClickListener {
            it.findNavController().navigate(R.id.action_packageFragment_to_packageProductsListing)
        }
    }

    private fun addPackage(
        Naziv: String,
        Opis: String,
        Popust: String,
        KolicinaPaketa: Int,
        Slika: File?
    ) {

        if (Slika != null) {
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), Slika)
            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("Slika", Slika.name, fileReqBody)

            val partToken = MultipartBody.Part.createFormData("Token", Session.user.Token)
            val partNaziv = MultipartBody.Part.createFormData("Naziv", Naziv)
            val partOpis = MultipartBody.Part.createFormData("Opis", Opis)
            val partPopust = MultipartBody.Part.createFormData("Popust", Popust)
            val partKolicinaPaketa = MultipartBody.Part.createFormData("KolicinaPaketa", KolicinaPaketa.toString())
            val partKorisnickoIme =
                MultipartBody.Part.createFormData("KorisnickoIme", Session.user.KorisnickoIme)

            mService.addNewPackageWithImage(
                partToken,
                true,
                partNaziv,
                partOpis,
                partPopust,
                part,
                partKorisnickoIme,
                partKolicinaPaketa
            ).enqueue(object :
                Callback<NewPackageResponse> {
                override fun onFailure(call: Call<NewPackageResponse>, t: Throwable) {
                    Toast.makeText(appContext, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewPackageResponse>,
                    response: Response<NewPackageResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "SUCCESS") {
                        Toast.makeText(
                            appContext,
                            "Paket uspješno dodan",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(appContext, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        appContext.startActivity(intent)
                        (appContext as Activity).overridePendingTransition(0, 0)
                        (appContext as Activity).finish()
                        (appContext as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent = Intent(appContext, LoginActivity::class.java)
                        Toast.makeText(
                            appContext,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity(appContext as Activity)
                    } else
                        Toast.makeText(
                            appContext,
                            response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT
                        ).show()
                }
            })


        } else {
            mService.addNewPackage(
                Session.user.Token,
                true,
                Naziv,
                Opis,
                Popust,
                Session.user.KorisnickoIme,
                KolicinaPaketa
            ).enqueue(object : Callback<NewPackageResponse> {
                override fun onFailure(call: Call<NewPackageResponse>, t: Throwable) {
                    Toast.makeText(appContext, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewPackageResponse>,
                    response: Response<NewPackageResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "PACKAGE ADDED") {
                        Toast.makeText(
                            appContext,
                            "Paket uspješno dodan",
                            Toast.LENGTH_SHORT
                        ).show()


                        val intent = Intent(appContext, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        appContext.startActivity(intent)
                        (appContext as Activity).overridePendingTransition(0, 0)
                        (appContext as Activity).finish()
                        (appContext as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent =
                            Intent(appContext, LoginActivity::class.java)
                        Toast.makeText(
                            appContext,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity(appContext as Activity)
                    } else
                        Toast.makeText(
                            appContext,
                            response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT
                        ).show()
                }
            })

        }
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
                    Toast.makeText(appContext, "Permission denied", Toast.LENGTH_SHORT).show()
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
                            appContext,
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

            val cursor = appContext.contentResolver.query(uri, filePathColumn, null, null, null)
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

    private fun pickImageFromGallery() {
        //Intent to pick image

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    private fun updatePackageWithImage(
        Id: Int,
        Naziv: String,
        Opis: String,
        Popust: String,
        Kolicina: Int,
        Slika: File?
    ) {
        //Kod za editiranje paketa čija je referenca trenutno spremljena u item varijablu
        if (packageUrl == "") {
            lateinit var part: MultipartBody.Part
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), Slika)
            part = MultipartBody.Part.createFormData("Slika", Slika?.name, fileReqBody)


            val partUpdate = MultipartBody.Part.createFormData("UPDATE", "true")
            val partToken = MultipartBody.Part.createFormData("Token", Session.user.Token)
            val partId = MultipartBody.Part.createFormData("Id", Id.toString())
            val partNaziv = MultipartBody.Part.createFormData("Naziv", Naziv)
            val partOpis = MultipartBody.Part.createFormData("Opis", Opis)
            val partPopust = MultipartBody.Part.createFormData("Popust", Popust)
            val partKolicina = MultipartBody.Part.createFormData("Kolicina", Kolicina.toString())
            val partKorisnickoIme =
                MultipartBody.Part.createFormData("KorisnickoIme", Session.user.KorisnickoIme)

            mService.updatePackageWithImage(
                partUpdate,
                partToken,
                partId,
                partNaziv,
                partOpis,
                partPopust,
                partKolicina,
                partKorisnickoIme,
                part
            ).enqueue(object :
                Callback<NewPackageResponse> {
                override fun onFailure(call: Call<NewPackageResponse>, t: Throwable) {
                    Toast.makeText(appContext, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewPackageResponse>,
                    response: Response<NewPackageResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "PACKAGE UPDATED") {
                        Toast.makeText(
                            appContext,
                            "Paket uspješno uređen",
                            Toast.LENGTH_SHORT
                        ).show()
                        activity!!.finish()
                        val intent = Intent(appContext, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        appContext.startActivity(intent)
                        (appContext as Activity).overridePendingTransition(0, 0)
                        (appContext as Activity).finish()
                        (appContext as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent = Intent(appContext, LoginActivity::class.java)
                        Toast.makeText(
                            appContext,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity(appContext as Activity)
                    } else
                        Toast.makeText(
                            appContext,
                            response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT
                        ).show()
                }
            })
        } else {
            mService.updatePackage(
                Session.user.Token,
                true,
                Id,
                Naziv,
                Opis,
                Kolicina.toString(),
                Popust,
                packageUrl,
                Session.user.KorisnickoIme
            ).enqueue(object : Callback<NewPackageResponse> {
                override fun onFailure(call: Call<NewPackageResponse>, t: Throwable) {
                    Toast.makeText(appContext, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<NewPackageResponse>,
                    response: Response<NewPackageResponse>
                ) {
                    if (response.body()!!.STATUSMESSAGE == "PACKAGE UPDATED") {
                        Toast.makeText(
                            appContext,
                            "Paket uspješno uređen",
                            Toast.LENGTH_SHORT
                        ).show()
                        activity!!.finish()
                        val intent = Intent(appContext, previousActivity)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        appContext.startActivity(intent)
                        (appContext as Activity).overridePendingTransition(0, 0)
                        (appContext as Activity).finish()
                        (appContext as Activity).overridePendingTransition(0, 0)
                    } else if (response.body()!!.STATUSMESSAGE == "OLD TOKEN") {
                        val intent =
                            Intent(appContext, LoginActivity::class.java)
                        Toast.makeText(
                            appContext,
                            "Sesija istekla, molimo prijavite se ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                        Session.reset()
                        startActivity(intent)
                        finishAffinity(appContext as Activity)
                    } else
                        Toast.makeText(
                            appContext,
                            response.body()!!.STATUSMESSAGE, Toast.LENGTH_SHORT
                        ).show()
                }
            })
        }


    }
}