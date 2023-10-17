package com.example.matching_manager.ui.team.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.databinding.TeamAddCategoryBinding
import com.example.matching_manager.ui.team.TeamAddActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView()= with(binding) {
        btnRecruitment.setOnClickListener {
            //용병 모집
            val intent = TeamAddActivity.newIntentForAddRecruit(requireActivity())
            requireActivity().startActivity(intent)
            dismiss()
        }

        btnApplication.setOnClickListener {
            //용병 신청
            val intent = TeamAddActivity.newIntentForAddApplication(requireActivity())
            requireActivity().startActivity(intent)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}