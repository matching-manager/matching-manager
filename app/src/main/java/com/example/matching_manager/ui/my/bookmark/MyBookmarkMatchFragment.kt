package com.example.matching_manager.ui.my.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matching_manager.databinding.MyBookmarkMatchFragmentBinding

class MyBookmarkMatchFragment : Fragment() {

    private var _binding: MyBookmarkMatchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyBookmarkMatchFragmentBinding.inflate(inflater, container, false)
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