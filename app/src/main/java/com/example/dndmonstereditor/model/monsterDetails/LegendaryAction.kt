package com.example.dndmonstereditor.model.monsterDetails



data class LegendaryAction(
    val damage: List<Damage>?=null,
    val dc: Dc?=null,
    val desc: String?=null,
    val name: String?=null
)