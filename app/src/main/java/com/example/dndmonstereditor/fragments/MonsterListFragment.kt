package com.example.dndmonstereditor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dndmonstereditor.R
import com.example.dndmonstereditor.adapter.MonsterListItemAdapter
import com.example.dndmonstereditor.adapter.OnMonsterClickListener
import com.example.dndmonstereditor.databinding.FragmentMonsterListBinding
import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.viewmodel.MonsterViewModel
import com.example.dndmonstereditor.viewmodel.States
import dagger.hilt.android.AndroidEntryPoint


//displays full list of monsters collected from the SRD
@AndroidEntryPoint
class MonsterListFragment : Fragment(), OnMonsterClickListener {

    private val monsterViewModel by viewModels<MonsterViewModel>()

    private val binding by lazy{
        FragmentMonsterListBinding.inflate(layoutInflater)
    }

    private val monsterAdapter by lazy{
        MonsterListItemAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val linearLayoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.MonsterList.apply{
            layoutManager = linearLayoutManager
            adapter = monsterAdapter
        }

        monsterViewModel.monstersLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is States.LOADING -> {}
                is States.SUCCESS -> {
                    val monsters = state.response
                        monsterAdapter.addToMonsterList(monsters.monsterFromLists)
                }
                is States.ERROR -> {
                    Toast.makeText(requireContext(), state.error.localizedMessage, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        if (monsterAdapter.itemCount==0) monsterViewModel.getMonsterList()

        return binding.root

    }


    override fun onClick(monsterFromList: MonsterFromList) {
        findNavController().navigate(R.id.action_MonsterListFragment_to_MonsterDetailsFragment, bundleOf(Pair("Monster",monsterFromList)))
    }

}