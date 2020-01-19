package com.example.webservice.Model

class Product(
    override val Id: Int?,
    override var Naziv: String?,
    override var Opis: String?,
    override var Slika: String?,
    var Kolicina: String,
    var Cijena: String?,
    var CijenaStavke: String?,
    override var expanded: Boolean = false,
    override var selected: Boolean = false
) : Item