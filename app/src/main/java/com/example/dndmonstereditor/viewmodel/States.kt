package com.example.dndmonstereditor.viewmodel

import com.example.dndmonstereditor.model.MonstersList
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import com.example.dndmonstereditor.roomdb.MonsterDBItem

sealed class States{
    object LOADING:States()
    class SUCCESS(val response: MonstersList): States()
    class SUCCESSDET(val response:MonsterDetails) :States()
    class SUCCESSNAME(val response:List<MonsterDBItem>):States()
    class ERROR(val error:Throwable):States()

}
