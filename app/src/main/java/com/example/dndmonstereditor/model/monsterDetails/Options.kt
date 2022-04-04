package com.example.dndmonstereditor.model.monsterDetails

import com.google.gson.annotations.SerializedName


data class Options(
    val choose: Int?=null,
   @SerializedName("from")
    val from: List<From>?=null
)