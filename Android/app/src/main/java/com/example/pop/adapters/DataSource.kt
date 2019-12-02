package com.example.pop.adapters

import com.example.database.Entities.Product

class DataSource{

    companion object{

        fun createDataSet(): ArrayList<Product>{
            val list = ArrayList<Product>()
            list.add(
                Product(
                    1,
                    "Sendvić",
                    9.90,
                    "Sendvić s pršutom",
                    "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fit,w_760,h_950/k%2FPhoto%2FRecipes%2F2019-07-how-to-monte-cristo-sandwich%2F190625-the-kitchn-christine-han-photography-116"

                )
            )
            list.add(
                Product(
                    2,
                    "Veliki sendvić",
                    15.00,
                    "Veliki sendvić s pršutom",
                    "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fit,w_760,h_950/k%2FPhoto%2FRecipes%2F2019-07-how-to-monte-cristo-sandwich%2F190625-the-kitchn-christine-han-photography-116"
                )
            )


            return list
        }
    }
}