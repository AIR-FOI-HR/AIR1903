package com.example.webservice.Model

import java.io.Serializable

class NewPackage {
    var NazivPaketa:String=""
    var Id_Proizvoda:String=""
    var Kolicina:String=""
    var Popust:String=""
}

class Package(val Id: Int, var NazivPaketa: String, var Id_Proizvoda: String, var Popust: String, var Kolicina:String="") :
    Serializable {
    var isSelected: Boolean = false
}