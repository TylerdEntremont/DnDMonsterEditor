package com.example.dndmonstereditor.modelhelpers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedMonsterChanges(
    val name: String,
    val ac:Int,
    val hp:Int,
    val attacks:String,
    val cr:String,
    val str:Int,
    val dex:Int,
    val con:Int,
    val intel:Int,
    val wis:Int,
    val cha:Int
):Parcelable
