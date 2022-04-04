package com.example.dndmonstereditor.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MonsterDBItem::class],version=1, exportSchema = false)
abstract class MonsterDB (): RoomDatabase() {

    abstract fun monsterDAO(): MonsterDAO

}
