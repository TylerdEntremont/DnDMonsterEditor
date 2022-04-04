package com.example.dndmonstereditor.model.monsterDetails

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize


data class Action(
    var attack_bonus: Int?,
    val attacks: List<Attack>,
    val damage: List<Damage>,
    val dc: Dc,
    val desc: String,
    val name: String,
    val options: Options,
    val usage: Usage
)