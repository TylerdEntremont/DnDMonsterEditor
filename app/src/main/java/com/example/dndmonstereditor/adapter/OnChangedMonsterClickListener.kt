package com.example.dndmonstereditor.adapter


import com.example.dndmonstereditor.roomdb.MonsterDBItem

interface OnChangedMonsterClickListener {
    fun onClick(monster: MonsterDBItem)
}