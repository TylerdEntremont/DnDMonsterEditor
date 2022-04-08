package com.example.dndmonstereditor.model.monsterDetails


data class Damage(
    var damage_dice: String?="0d0+0",
    val damage_type: DamageType,
)