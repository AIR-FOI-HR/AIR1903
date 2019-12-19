package com.example.webservice.Model

import java.io.Serializable

class NewPackage {
    var Naziv:String=""
    var Opis:String=""
    var Slika:String=""
    var Kolicina:String=""
    var Popust:String=""
    var KolicinaPaketa=""
}
class Package(val Id: Int, var Naziv: String, var Opis: String, var Popust: String, var Slika:String="", var KolicinaPaketa: String) :
    Serializable {
    var isSelected: Boolean = false
}