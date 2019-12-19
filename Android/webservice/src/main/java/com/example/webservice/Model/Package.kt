package com.example.webservice.Model

class Package(var Popust: Double  = 0.00,
              override val Id: Int?,
              override var Naziv: String = "",
              override var Opis: String? = "",
              override var Slika: String? = "",
              override var expanded: Boolean = false,
              override var selected: Boolean = false
) : Item
