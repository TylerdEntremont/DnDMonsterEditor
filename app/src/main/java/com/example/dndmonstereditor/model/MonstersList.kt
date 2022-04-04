package com.example.dndmonstereditor.model

import com.google.gson.annotations.SerializedName


data class MonstersList(
    val count: Int,
    @SerializedName("results")
    val monsterFromLists: List<MonsterFromList>
)