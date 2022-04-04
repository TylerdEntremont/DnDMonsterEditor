package com.example.dndmonstereditor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dndmonstereditor.databinding.MonsterListItemBinding
import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.model.MonstersList
import retrofit2.Response

class MonsterListItemAdapter(
    private val monsterClickListener: OnMonsterClickListener,
    private var monsterList:MutableList<MonsterFromList> = mutableListOf()
):RecyclerView.Adapter<MonsterViewHolder>() {


    fun addToMonsterList (monsters: List<MonsterFromList>){
            monsterList.clear()
            monsterList.addAll(monsters)
            notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val monsterBinding =
            MonsterListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MonsterViewHolder(monsterBinding,monsterClickListener)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monster = monsterList[position]
        holder.bind(monster)
    }

    override fun getItemCount(): Int =monsterList.size

}

class MonsterViewHolder(
    private val binding:MonsterListItemBinding,
    private val onClick:OnMonsterClickListener):RecyclerView.ViewHolder(binding.root){

    fun bind (monster: MonsterFromList){
        binding.MonsterName.text=monster.name
        binding.root.setOnClickListener {
            onClick.onClick(monster)
        }
    }
}