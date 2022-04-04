package com.example.dndmonstereditor.viewmodel

import com.example.dndmonstereditor.model.MonstersList
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails

sealed class States{
    object LOADING:States()
    class SUCCESS(val response: MonstersList): States()
    class SUCCESSDET(val response:MonsterDetails) :States()
    class ERROR(val error:Throwable):States()

}
