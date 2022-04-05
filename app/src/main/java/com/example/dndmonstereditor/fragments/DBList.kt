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
import com.example.dndmonstereditor.adapter.DBMonsterAdapter
import com.example.dndmonstereditor.adapter.OnChangedMonsterClickListener
import com.example.dndmonstereditor.databinding.FragmentDbListBinding
import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.roomdb.MonsterDBItem
import com.example.dndmonstereditor.viewmodel.MonsterViewModel
import com.example.dndmonstereditor.viewmodel.States
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DBList : Fragment(), OnChangedMonsterClickListener {

    private val monsterViewModel by viewModels<MonsterViewModel>()


    private val binding by lazy{
        FragmentDbListBinding.inflate(layoutInflater)
    }

    private val monsterAdapter by lazy{
        DBMonsterAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
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
                is States.SUCCESSNAME -> {
                    val monsters = state.response
                    monsterAdapter.addToMonsterList(monsters)
                }
                is States.ERROR -> {
                    Toast.makeText(requireContext(), state.error.localizedMessage, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        monsterViewModel.getNames()


        return binding.root
    }

    override fun onClick(monster: MonsterDBItem) {
        findNavController().navigate(R.id.action_DBList_to_MonsterDetailsFragment, bundleOf(Pair("Monster",MonsterFromList(monster.monster.name,"","")),Pair("Changes",monster)))
    }


}