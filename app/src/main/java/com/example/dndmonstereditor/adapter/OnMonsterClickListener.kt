package com.example.dndmonstereditor.adapter

import com.example.dndmonstereditor.model.MonsterFromList

interface OnMonsterClickListener {
    fun onClick (monsterFromList: MonsterFromList)
}