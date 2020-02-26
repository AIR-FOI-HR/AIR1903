package com.example.webservice.Model

import java.io.Serializable
import java.util.*

class Store (
    val Id_Trgovine : Int?,
    var NazivTrgovine: String?,
    var BrojZaposlenika:Int?,
    var selected: Boolean = false
) : Serializable

