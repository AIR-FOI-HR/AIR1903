package com.example.webservice.Model

class PackageClass(
    var Popust: String = "",
    var Kolicina: String? = "",
    override val Id: Int?,
    override var Naziv: String = "",
    override var Opis: String? = "",
    override var Slika: String? = "",
    var Items: List<Product>?,
    override var expanded: Boolean = false,
    override var selected: Boolean = false
) : Item

