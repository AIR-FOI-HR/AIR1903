package com.example.webservice.Model

class Product(val Id: Int, var Naziv: String, var Cijena: Double, var Opis: String?, var Slika: String){
    var isExpanded: Boolean = false
}