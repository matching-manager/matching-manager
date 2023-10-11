package com.example.matching_manager.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.R
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.example.matching_manager.databinding.TeamFragmentBinding
import com.example.matching_manager.ui.my.MyFragment

class TeamFragment : Fragment() {
    private var _binding: TeamFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = TeamFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TeamFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}