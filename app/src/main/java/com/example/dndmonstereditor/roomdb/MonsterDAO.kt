package com.example.dndmonstereditor.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MonsterDAO {

    @Query ("SELECT * FROM savedMonsterDB")
    fun getNames(): Flow<List<MonsterDBItem>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (monsterDBItem: MonsterDBItem)
}