package com.example.dndmonstereditor.modelhelpers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedMonsterChanges(
    val name: String,
    val ac:Int,
    val hp:Int,
    val attacks:String
):Parcelable
