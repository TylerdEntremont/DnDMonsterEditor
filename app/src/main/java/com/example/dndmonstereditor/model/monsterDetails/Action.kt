package com.example.dndmonstereditor.model.monsterDetails

import com.google.gson.annotations.SerializedName


data class Action(
    var attack_bonus: Int?,
    val attacks: List<Attack>,
    val damage: List<Damage>,
    val dc: Dc,
    var desc: String,
    val name: String,
    val options: Options,
    val usage: Usage
)