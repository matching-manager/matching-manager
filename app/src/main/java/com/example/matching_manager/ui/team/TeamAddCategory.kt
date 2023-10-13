package com.example.matching_manager.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.databinding.MatchCategoryBinding
import com.example.matching_manager.databinding.TeamAddCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TeamAddCategory : BottomSheetDialogFragment() {

    private var _binding: TeamAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamAddCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}