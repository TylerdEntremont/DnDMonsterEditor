package com.example.dndmonstereditor.rest

import com.example.dndmonstereditor.model.MonstersList
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

    @GET(MONSTERS_PATH)
    suspend fun getMonsterList():Response<MonstersList>

    @GET(MONSTERS_PATH + "{monsterName}")
    suspend fun getMonsterDetails(
        @Path("monsterName") monsterName:String
    ): Response<MonsterDetails>


    companion object{
        const val BASE_URL = "https://www.dnd5eapi.co"
        private const val MONSTERS_PATH ="/api/monsters/"
    }
}