package com.example.dndmonstereditor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dndmonstereditor.R
import com.example.dndmonstereditor.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private val binding by lazy{
        FragmentMainBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFL.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_MonsterListFragment)
        }

        binding.buttonSI.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_DBList)
        }
    }

}