package com.example.webservice.Model

import java.io.Serializable
import java.util.*

class Invoice (
    val Id : Int?,
    var MjestoIzdavanja : String?,
    var DatumIzdavanja : String?,
    var Popust : Double?,
    var Id_Trgovine : Int?,
    var Kupac : Int?
) : Serializable