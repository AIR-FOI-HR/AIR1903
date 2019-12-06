package com.example.pop_sajamv2

import com.example.webservice.Model.User

object Session {
    var user:User=User()
    fun reset(){
        user.reset()
    }
}