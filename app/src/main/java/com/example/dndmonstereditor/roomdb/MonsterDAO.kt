package com.example.dndmonstereditor.roomdb

import androidx.room.*
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import io.reactivex.Single

@Dao
interface MonsterDAO {

    @Query ("SELECT uniqueName FROM savedMonsterDB")
    fun getNames():Single<List<String>>

    @RewriteQueriesToDropUnusedColumns
    @Query ("SELECT * FROM savedMonsterDB WHERE uniqueName is :name")
    fun getMonster(name:String):Single<SavedMonsterChanges>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (monsterDBItem: MonsterDBItem)
}