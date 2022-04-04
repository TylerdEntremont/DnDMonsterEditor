package com.example.dndmonstereditor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dndmonstereditor.databinding.MonsterListItemBinding
import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import com.example.dndmonstereditor.roomdb.MonsterDBItem

class DBMonsterAdapter(
    private val monsterClickListener: OnChangedMonsterClickListener,
    private var monsterList:MutableList<MonsterDBItem> = mutableListOf()
): RecyclerView.Adapter<DBMonsterViewHolder>() {


    fun addToMonsterList (monsters: List<MonsterDBItem>){
        monsterList.clear()
        monsterList.addAll(monsters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBMonsterViewHolder {
        val monsterBinding =
            MonsterListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DBMonsterViewHolder(monsterBinding,monsterClickListener)
    }

    override fun onBindViewHolder(holder: DBMonsterViewHolder, position: Int) {
        val monster = monsterList[position]
        holder.bind(monster)
    }

    override fun getItemCount(): Int =monsterList.size

}

class DBMonsterViewHolder(
    private val binding: MonsterListItemBinding,
    private val onClick:OnChangedMonsterClickListener): RecyclerView.ViewHolder(binding.root){

    fun bind (monster: MonsterDBItem){
        binding.MonsterName.text=monster.uniqueName
        binding.root.setOnClickListener {
            onClick.onClick(monster)
        }
    }
}