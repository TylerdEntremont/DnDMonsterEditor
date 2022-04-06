package com.example.dndmonstereditor.adapter

import com.example.dndmonstereditor.model.MonsterFromList

//on click listener interface for the monsters in the Monster List Fragment
interface OnMonsterClickListener {
    fun onClick (monsterFromList: MonsterFromList)
}