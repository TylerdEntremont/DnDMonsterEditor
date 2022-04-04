package com.example.dndmonstereditor.roomdb

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import kotlinx.parcelize.Parcelize

@Entity(tableName = "savedMonsterDB")
@Parcelize
data class MonsterDBItem (
   @PrimaryKey val uniqueName:String="",
    @Embedded val monster: SavedMonsterChanges
):Parcelable
