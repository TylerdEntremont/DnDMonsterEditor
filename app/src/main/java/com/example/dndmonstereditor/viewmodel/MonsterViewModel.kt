package com.example.dndmonstereditor.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.room.Room
import com.example.dndmonstereditor.rest.MonsterRepository
import com.example.dndmonstereditor.roomdb.MonsterDB
import com.example.dndmonstereditor.roomdb.MonsterDBItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonsterViewModel @Inject constructor (
    private val monsterRepository: MonsterRepository,
    private var coroutineDispatcher: CoroutineDispatcher,
    @ApplicationContext
    private val context: Context
): ViewModel() {
    private val _monstersLiveData: MutableLiveData<States> = MutableLiveData(States.LOADING)
    val monstersLiveData : LiveData<States> get() = _monstersLiveData

    private val monsterDB= context.let { Room.databaseBuilder(it, MonsterDB::class.java,"savedMonsterDB").allowMainThreadQueries().build()}


    fun getMonsterList(){
        _monstersLiveData.postValue(States.LOADING)


        viewModelScope.launch(coroutineDispatcher) {
            monsterRepository.getMonsterList()
                .catch{ _monstersLiveData.postValue(States.ERROR(it))}
                .collect { state->
                _monstersLiveData.postValue(state)
            }
        }
    }

    fun getMonsterDetails(monsterName:String){
        _monstersLiveData.postValue(States.LOADING)
        viewModelScope.launch(coroutineDispatcher){
            monsterRepository.getMonsterDetails(monsterName)
                .catch{_monstersLiveData.postValue((States.ERROR(it)))}
                .collect { state->
                _monstersLiveData.postValue(state)
            }
        }
    }

    fun saveToDataBase(data:MonsterDBItem) {
        monsterDB.monsterDAO().insert(data)
    }

    fun getNames(){

        _monstersLiveData.postValue(States.LOADING)
        viewModelScope.launch(coroutineDispatcher){
            monsterDB.monsterDAO().getNames()
                .catch{_monstersLiveData.postValue((States.ERROR(it)))}
                .collect { response->
                    _monstersLiveData.postValue(States.SUCCESSNAME(response))
                }
        }
    }

}