package com.example.matching_manager.ui.home.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.AnnouncementFragmentBinding


class AnnouncementFragment : DialogFragment() {

    private var _binding: AnnouncementFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModels { HomeViewModelFactory() }

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

        btnCancel.setOnClickListener {
            onDestroyView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}