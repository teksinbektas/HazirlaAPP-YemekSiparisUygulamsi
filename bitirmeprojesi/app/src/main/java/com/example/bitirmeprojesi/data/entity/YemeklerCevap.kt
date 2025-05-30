package com.example.bitirmeprojesi.data.entity

import com.google.gson.annotations.SerializedName

data class YemeklerCevap(
    @SerializedName("yemekler")
    val yemekler: List<Yemekler>,

    @SerializedName("success")
    val success: Int
)

