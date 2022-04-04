package com.example.dndmonstereditor.roomdb

import androidx.room.*
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import io.reactivex.Single

@Dao
interface MonsterDAO {

    @Query ("SELECT * FROM savedMonsterDB")
    fun getNames():Single<List<MonsterDBItem>>

    @RewriteQueriesToDropUnusedColumns
    @Query ("SELECT * FROM savedMonsterDB WHERE uniqueName is :name")
    fun getMonster(name:String):Single<SavedMonsterChanges>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (monsterDBItem: MonsterDBItem)
}