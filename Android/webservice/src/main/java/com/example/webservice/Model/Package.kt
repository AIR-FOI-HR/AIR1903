package com.example.webservice.Model

class Package(
    var Popust: String? = "",
    var KolicinaPaketa: String? = "",
    override val Id: Int?,
    override var Naziv: String = "",
    override var Opis: String? = "",
    override var Slika: String? = "",
    override var expanded: Boolean = false,
    override var selected: Boolean = false
) : Item

