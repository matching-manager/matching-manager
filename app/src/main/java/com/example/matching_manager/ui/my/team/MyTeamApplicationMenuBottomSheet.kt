package com.example.matching_manager.ui.my.team

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.matching_manager.databinding.MyTeamApplicationMenuBottomSheetBinding
import com.example.matching_manager.ui.my.team.MyTeamApplicationEditActivity.Companion.editIntent
import com.example.matching_manager.ui.team.TeamItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyTeamApplicationMenuBottomSheet(private val item : TeamItem.ApplicationItem) : BottomSheetDialogFragment() {
    private var _binding: MyTeamApplicationMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MyTeamApplicationMenuBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        tvEdit.setOnClickListener {
            resultLauncher.launch(editIntent(requireContext(), item))
        }
        tvRemove.setOnClickListener {
            val dialog = MyTeamApplicationDeleteDialog(item)
            dialog.show(childFragmentManager, "deleteDialog")
            dialog.setOnDismissListener(object :
                MyTeamApplicationDeleteDialog.OnDialogDismissListener {
                override fun onDismiss() {
                    dismiss()
                }
            })
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}