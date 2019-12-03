package com.example.pop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.database.Entities.Product
import kotlinx.android.synthetic.main.activity_manage_products.*

class ManageProductsActivity : AppCompatActivity() {
    private lateinit var product : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        if(intent.hasExtra("productId")) {
            layoutManageProductsButtonSubmit.text = getString(R.string.buttonSave)
            if(getProduct(intent.getIntExtra("productId", -1))) {
                layoutManageProductsInputName.setText(product.Naziv)
                layoutManageProductsInputValue.setText(product.Cijena.toString())
                layoutManageProductsImage.setImageURI(product.Slika?.toUri())
            }
        }
        else {
            layoutManageProductsButtonSubmit.text = getString(R.string.buttonAdd)
        }
    }

    private fun getProduct(id : Int) : Boolean{
        if(id != -1) {
            //Tu treba popunit varijablu product sa podacima iz baze na temelju id-a
            return true
        }
        return false
    }
}