package com.example.matching_manager.ui.team.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.matching_manager.databinding.TeamAddCategoryBinding
import com.example.matching_manager.ui.team.TeamFragment.Companion.FRAGMENT_REQUEST_KEY
import com.example.matching_manager.ui.team.TeamFragment.Companion.FRAGMENT_RETURN_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TeamAddCategoryBottomSheet : BottomSheetDialogFragment() {

    private var _binding: TeamAddCategoryBinding? = null
    private val binding get() = _binding!!

    companion object{
        const val RETURN_TYPE_RECRUITMENT = "return_type_recruitment"
        const val RETURN_TYPE_APPLICATION = "return_type_application"
    }

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

    private fun initView() = with(binding) {
        // 여기서 Fragment Result Listener로 값을 return한다.
        btnRecruitment.setOnClickListener {
            //용병 모집
            setFragmentResult(FRAGMENT_REQUEST_KEY,bundleOf( FRAGMENT_RETURN_TYPE to RETURN_TYPE_RECRUITMENT))
            dismiss()
            Log.d("teamAddCaregory","close bottonseet")
        }

        btnApplication.setOnClickListener {
            //용병 신청
            setFragmentResult(FRAGMENT_REQUEST_KEY,bundleOf(FRAGMENT_RETURN_TYPE to RETURN_TYPE_APPLICATION))
            dismiss()
            Log.d("teamAddCaregory","close bottonseet")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}