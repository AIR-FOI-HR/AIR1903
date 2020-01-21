package com.example.webservice.Model

import java.io.Serializable
import java.util.*

class Invoice (
    val Id : Int?,
    var MjestoIzdavanja : String?,
    var DatumIzdavanja : String?,
    var Id_Trgovine : Int?,
<<<<<<< HEAD
    var Kupac : Int?
) : Serializable
=======
    var Trgovina : String?,
    var Kupac : Int?,
    var Ime_Klijenta: String?,
    var Prezime_Klijenta: String?,
    var KorisnickoIme: String?,
    var CijenaRacuna: String?,
    var PopustRacuna: String?,
    var IznosPopustaRacuna: String?,
    var ZavrsnaCijena: String?,
    var Stavke: List<Item>?
)
>>>>>>> origin/android/feature/invoices/restful
