package com.example.matching_manager.ui.my.match

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.matching_manager.databinding.MyMatchMenuBottomSheetBinding
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.my.my.MyDeleteDialog
import com.example.matching_manager.ui.my.my.MyFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyMatchMenuBottomSheet(private val item : MatchDataModel) : BottomSheetDialogFragment() {

    private var _binding: MyMatchMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MyMatchMenuBottomSheetBinding.inflate(inflater, container, false)

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
            val dialog = MyDeleteDialog(item)
            dialog.show(childFragmentManager, "deleteDialog")
            dialog.setOnDismissListener(object : MyDeleteDialog.OnDialogDismissListener {
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


    private fun editIntent(context: Context, item: MatchDataModel): Intent {
        val intent = Intent(context, MyMatchEditActivity::class.java)
        intent.putExtra(MyFragment.OBJECT_DATA, item)
        return intent
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}