package com.example.webservice.Model

class ProductResponse {
    var STATUS:Boolean=false
    var STATUSMESSAGE:String?=null
    lateinit var DATA : List<Product>
}