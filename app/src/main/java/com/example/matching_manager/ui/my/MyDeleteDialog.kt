package com.example.matching_manager.ui.my

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.matching_manager.databinding.MyDeleteDialogBinding

class MyDeleteDialog(private val item: MyMatchDataModel) : DialogFragment() {
    private var _binding: MyDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private var dismissListener: OnDialogDismissListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        viewModel.event.observe(this) {
            when (it) {
                is MatchEvent.Dismiss -> {
                    dismiss()
                }

                is MatchEvent.Finish -> {
                }
            }
        }
    }

    private fun initView() = with(binding) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialBtn1.setOnClickListener {
            viewModel.deleteMatch(item)
        }

        binding.dialBtn2.setOnClickListener {
            dismiss()
        }
    }

    interface OnDialogDismissListener {
        fun onDismiss()
    }

    fun setOnDismissListener(listener: OnDialogDismissListener) {
        dismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // 다이얼로그가 닫힐 때 작업을 수행한 후 액티비티에 알림
        dismissListener?.onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}