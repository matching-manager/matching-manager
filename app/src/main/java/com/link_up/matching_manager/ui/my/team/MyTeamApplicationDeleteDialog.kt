package com.link_up.matching_manager.ui.my.team

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
import com.link_up.matching_manager.databinding.MyTeamApplicationDeleteDialogBinding
import com.link_up.matching_manager.ui.my.my.MyEvent
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.link_up.matching_manager.ui.my.my.MyViewModelFactory
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyTeamApplicationDeleteDialog(private val item: TeamItem.ApplicationItem) : DialogFragment() {
    private var _binding: MyTeamApplicationDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyViewModelFactory()
    }

    private var dismissListener: OnDialogDismissListener? = null

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyTeamApplicationDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel){
        event.observe(this@MyTeamApplicationDeleteDialog) {
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

    private fun deleteFromFireBase(item: TeamItem.ApplicationItem) {
        val fileRef = reference.child("Team/${item.teamId}")

        if(item.postImg == "") {
            viewModel.deleteApplication(item)
        }
        else {
            binding.progressBar.visibility = View.VISIBLE

            fileRef.delete()
                .addOnSuccessListener {
                    viewModel.deleteApplication(item)
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "게시글 삭제를 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }
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