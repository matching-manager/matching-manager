package com.example.matching_manager.ui.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyFragmentBinding
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.example.matching_manager.ui.match.MatchFragment

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}