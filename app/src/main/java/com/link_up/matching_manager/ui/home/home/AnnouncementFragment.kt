package com.link_up.matching_manager.ui.home.home

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.link_up.matching_manager.databinding.AnnouncementFragmentBinding


class AnnouncementFragment : DialogFragment() {

    private var _binding: AnnouncementFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }

    private val receivedData = arguments?.getParcelable<AnnouncementDataModel>(OBJECT_DATA)

    companion object {
        const val OBJECT_DATA = "item_object"
        fun newInstance(data: AnnouncementDataModel): AnnouncementFragment {
            val fragment = AnnouncementFragment()
            val args = Bundle()
            args.putParcelable(OBJECT_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AnnouncementFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel){
        announceModel.observe(viewLifecycleOwner, Observer {
            // TODO : 요기서 가져온 아이템으로 데이터를 뿌려주시면 됩니다.
        })
    }

    private fun initView() = with(binding) {
        // Dialog의 배경을 투명으로 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Log.d("1234", "${receivedData?.announceNum}")

        tvTitle.text = receivedData?.title
        tvDate.text = receivedData?.uploadDate
        tvContent.text = receivedData?.content

        btnCancel.setOnClickListener {
            onDestroyView()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.9F, 0.8F)
    }

    private fun Context.dialogFragmentResize(
        dialogFragment: DialogFragment,
        width: Float,
        height: Float
    ) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}