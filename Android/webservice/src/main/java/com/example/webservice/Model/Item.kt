package com.example.webservice.Model

import java.io.Serializable

interface Item : Serializable {
    val Id: Int?
    var Naziv: String?
    var Opis: String?
    var Slika: String?
    var Kolicina: String?

    var expanded: Boolean
    var selected: Boolean
}

