package com.link_up.matching_manager.ui.home.arena.detail_dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.link_up.matching_manager.databinding.ArenaDetailFragmentBinding
import com.link_up.matching_manager.ui.home.arena.ArenaViewModel
import com.link_up.matching_manager.util.Utils

class ArenaDetailFragment : DialogFragment() {

    private var _binding : ArenaDetailFragmentBinding? = null

    private val binding get() = _binding!!

    private val sharedViewModel : ArenaViewModel by activityViewModels()

    private val viewModel : ArenaDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArenaDetailFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModels()
    }

    private fun initViewModels() = with(binding){
        with(sharedViewModel){
            item.observe(viewLifecycleOwner, Observer { item ->
                viewModel.updateItemDetail(item)
            })
        }
        with(viewModel){
            detailItem.observe(viewLifecycleOwner, Observer {
                tvTitle.text = it.placeName
                tvLocation.text = it.addressName
                tvCall.text = it.phone
            })
        }
    }

    private fun initView() = with(binding){



        // Dialog의 배경을 투명으로 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnShare.setOnClickListener {
            Utils.shareUrl(requireContext(),viewModel.getPlaceUrl())
        }
        btnCall.setOnClickListener {

            activity?.let { activity ->
                viewModel.getPhoneNumber()?.let { phoneNumber ->
                    Utils.callPhoneNumber(activity, phoneNumber)
                } ?: run {
                    Toast.makeText(requireContext(), "전화번호가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnCancel.setOnClickListener {
            val fragment = requireActivity().supportFragmentManager.findFragmentByTag("SampleDialog")
            if (fragment != null) {
                requireActivity().supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}