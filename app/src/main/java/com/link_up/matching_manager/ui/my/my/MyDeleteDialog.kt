package com.link_up.matching_manager.ui.my.my

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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.link_up.matching_manager.databinding.MyDeleteDialogBinding
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyDeleteDialog(private val item: MatchDataModel) : DialogFragment() {
    private var _binding: MyDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPostViewModel by viewModels {
        MyPostViewModelFactory()
    }

    private var dismissListener: OnDialogDismissListener? = null

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

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
            deleteFromFireBase(item)
        }

        binding.dialBtn2.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.9F, 0.2F)
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

    private fun deleteFromFireBase(item: MatchDataModel) {
        val fileRef = reference.child("Match/${item.matchId}")

        if(item.postImg == "") {
            viewModel.deleteMatch(item)
        }
        else {
            binding.progressBar.visibility = View.VISIBLE

            fileRef.delete()
                .addOnSuccessListener {
                    viewModel.deleteMatch(item)
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "게시글 삭제를 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }
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