package com.example.pop_sajamv2

import com.example.webservice.Model.User
import java.math.BigInteger

object Session {
    var user:User=User()
    val expander = BigInteger("10366590174751098504021101516526571562453730560926949855400199801242584769764638222759018763045057889411844305742024015554773846491506097706516145168109883")
    fun reset(){
        user.reset()
    }
    val productsEng="Products"
    val packagesEng="Packages"
    val productsHrv="Proizvodi"
    val packagesHrv="Paketi"
}