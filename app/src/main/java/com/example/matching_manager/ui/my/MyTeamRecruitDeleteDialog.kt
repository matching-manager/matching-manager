package com.example.matching_manager.ui.my

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.matching_manager.databinding.MyTeamRecruitDeleteDialogBinding
import com.example.matching_manager.ui.team.TeamItem

class MyTeamRecruitDeleteDialog(private val item: TeamItem.RecruitmentItem) : DialogFragment() {
    private var _binding: MyTeamRecruitDeleteDialogBinding? = null
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
        _binding = MyTeamRecruitDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        event.observe(this@MyTeamRecruitDeleteDialog) {
            when (it) {
                is MyEvent.Dismiss -> {
                    dismiss()
                }

                is MyEvent.Finish -> {
                }
            }
        }
    }
    private fun initView() = with(binding) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialBtn1.setOnClickListener {
            viewModel.deleteRecruit(item)
        }

        binding.dialBtn2.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.7F, 0.2F)
    }

    private fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
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