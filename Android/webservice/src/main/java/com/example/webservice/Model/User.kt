package com.example.webservice.Model

class User {
    var Ime:String=""
    var Prezime:String=""
    var Email:String=""
    var KorisnickoIme:String=""
    var StanjeRacuna:String=""
    var DozvolaUpravljanjeUlogama:String=""
    var DozvolaUpravljanjeStanjemRacuna:String=""
    var DozvolaPregledTransakcija:String=""
    var DozvolaUvidUStatistiku:String=""
    var Id_Uloge:Int=0
    var Naziv_Uloge:String=""
    var LoginTime:Int=0
    var Token:String=""
    var Jezik:Int=0

    fun reset(){
        Ime=""
        Prezime=""
        Email=""
        KorisnickoIme=""
        StanjeRacuna=""
        DozvolaUpravljanjeUlogama=""
        DozvolaUpravljanjeStanjemRacuna=""
        DozvolaPregledTransakcija=""
        DozvolaUvidUStatistiku=""
        Id_Uloge=0
        Naziv_Uloge=""
        LoginTime=0
        Token=""
        Jezik=0
    }

}