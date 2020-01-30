package com.example.pop

import com.example.webservice.Model.Item
import java.io.Serializable

class ItemsWrapper(items : ArrayList<Item>) : Serializable {
    private var itemsArray : ArrayList<Item> = items
    fun getItems() : ArrayList<Item>{
        return itemsArray
    }
}