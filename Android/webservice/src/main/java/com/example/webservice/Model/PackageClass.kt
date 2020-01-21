package com.example.webservice.Model

class PackageClass(
    override val Id: Int?,
    override var Naziv: String?,
    override var Opis: String?,
    override var Slika: String?,
    override var Kolicina: String?,
    var CijenaStavke: String?,
    var Popust: String?,
    var IznosPopusta: String?,
    var CijenaStavkeNakonPopusta: String?,
    var StavkePaketa: List<Product>?,
    override var expanded: Boolean = false,
    override var selected: Boolean = false
) : Item

