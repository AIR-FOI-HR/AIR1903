package com.example.webservice.Model

import java.io.Serializable
import java.util.*

class Store (
    val Id : Int?,
    var Naziv: String?,
    var selected: Boolean = false
) : Serializable

