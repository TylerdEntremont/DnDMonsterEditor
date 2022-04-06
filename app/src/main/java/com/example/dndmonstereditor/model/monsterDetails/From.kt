package com.example.dndmonstereditor.model.monsterDetails


import com.google.gson.annotations.SerializedName


data class From(
    @SerializedName("0")
    val a: X=X(0,",",""),
    @SerializedName("1")
    val b: X=X(0,",",""),
    @SerializedName("2")
    val c: X=X(0,",",""),
    @SerializedName("3")
    val d: X=X(0,",",""),
    @SerializedName("4")
    val e: X=X(0,",","")
)