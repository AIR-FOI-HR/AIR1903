package com.example.database.Entities

class ProductResponse {
    var STATUS:Boolean=false
    var STATUSMESSAGE:String?=null
    lateinit var DATA : List<Product>
}