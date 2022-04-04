package com.example.dndmonstereditor.model.monsterDetails

data class SpecialAbility(
    val damage: List<Damage>?=null,
    val desc: String?=null,
    val name: String?=null,
    val usage: UsageSA?=null
)