package com.example.dndmonstereditor.adapter


import com.example.dndmonstereditor.roomdb.MonsterDBItem

//on click listener interface for the monsters in the DBList
interface OnChangedMonsterClickListener {
    fun onClick(monster: MonsterDBItem)
}