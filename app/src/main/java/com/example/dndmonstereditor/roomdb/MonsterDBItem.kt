package com.example.dndmonstereditor.roomdb

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges

@Entity(tableName = "savedMonsterDB")
data class MonsterDBItem (
   @PrimaryKey val uniqueName:String="",
    @Embedded val monster: SavedMonsterChanges
)
