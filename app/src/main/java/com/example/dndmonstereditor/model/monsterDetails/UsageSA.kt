package com.example.dndmonstereditor.model.monsterDetails

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize

data class UsageSA(
    val rest_types: List<String>?=null,
    val times: Int?=null,
    val type: String?=null
)