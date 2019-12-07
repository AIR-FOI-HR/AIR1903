package com.example.webservice.Model

import java.io.Serializable

class Product(val Id: Int, var Naziv: String, var Cijena: Double, var Opis: String?, var Slika: String) : Serializable {
    var isExpanded: Boolean = false
}
class NewProduct{
    var Naziv:String=""
    var Cijena:String=""
    var Opis:String=""
    var Slika:String=""
}