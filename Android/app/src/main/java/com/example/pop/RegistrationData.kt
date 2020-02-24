package com.example.pop

object RegistrationData {
    var Ime:String=""
    var Prezime:String=""
    var KorisnickoIme:String=""
    var Email:String=""
    var Lozinka:String=""
    var Uloga:Int=0
    var Trgovina:String = ""

    fun Reset(){
        Ime=""
        Prezime=""
        KorisnickoIme=""
        Email=""
        Lozinka=""
        Uloga=0
        Trgovina= ""
    }

}