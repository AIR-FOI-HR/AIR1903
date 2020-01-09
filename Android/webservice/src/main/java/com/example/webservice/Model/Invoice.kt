package com.example.webservice.Model

import java.util.*

class Invoice (
    val Id : Int,
    var MjestoIzdavanja : String,
    var DatumIzdavanja : Date,
    var Popust : Double,
    var Id_Trgovine : Int,
    var Kupac : Int
)