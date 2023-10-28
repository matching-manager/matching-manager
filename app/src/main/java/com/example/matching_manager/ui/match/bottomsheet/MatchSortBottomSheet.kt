package com.example.matching_manager.ui.match.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.databinding.MatchSortBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MatchSortBottomSheet : BottomSheetDialogFragment() {

    private var _binding: MatchSortBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MatchSortBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}