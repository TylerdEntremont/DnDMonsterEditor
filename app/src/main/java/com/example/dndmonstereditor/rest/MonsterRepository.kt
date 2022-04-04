package com.example.dndmonstereditor.rest


import com.example.dndmonstereditor.viewmodel.States
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface MonsterRepository {
    fun getMonsterList():Flow<States>
    fun getMonsterDetails(monsterName:String):Flow<States>
}

class MonsterRepositoryImpl(
    private val service: Service
):MonsterRepository{


   override fun getMonsterList():Flow<States> {
       return flow{
           try {
               val response = service.getMonsterList()
               if (response.isSuccessful) {
                   response.body()?.let {
                           emit(States.SUCCESS(it))
                   } ?: throw Exception("Response null")
               } else {
                   throw Exception("No success")
               }
           } catch (e: Exception) {
               //emit(States.ERROR(e))
           }
       }
   }

    override fun getMonsterDetails(monsterName: String): Flow<States> {
        return flow{
            //try {
                val response = service.getMonsterDetails(monsterName)
                if (response.isSuccessful) {
                    response.body()?.let {
                            emit(States.SUCCESSDET(it))
                    } ?: throw Exception("Response null")
                } else {
                    throw Exception("No success")
                }
            //} catch (e: Exception) {
                //emit(States.ERROR(e))
            //}
        }
    }

}