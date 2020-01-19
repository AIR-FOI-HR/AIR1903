package com.example.webservice

import com.example.webservice.Model.Item
import com.example.webservice.Model.PackageClass
import com.example.webservice.Model.Product
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type


internal class ItemJsonDeserializer : JsonDeserializer<Item> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
        val type = json.asJsonObject.get("ItemType").asString
        when (type) {
            "Proizvod" -> return context.deserialize<Item>(json, Product::class.java)
            "Paket" -> return context.deserialize<Item>(json, PackageClass::class.java)
            else -> throw IllegalArgumentException("Nije ni proizvod ni paket")
        }
    }
}