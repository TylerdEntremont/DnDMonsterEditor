package com.example.dndmonstereditor.adapter

import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import com.example.dndmonstereditor.roomdb.MonsterDBItem

interface OnChangedMonsterClickListener {
    fun onClick(monsterFromList: MonsterDBItem)
}