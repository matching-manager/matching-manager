package com.example.matching_manager.ui.signin

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.matching_manager.R
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.example.matching_manager.databinding.SignUpFragmentBinding


class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel :SignInSharedViewModel by activityViewModels { SignInViewModelFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding){
        // 연락처 입력 시 하이픈 자동 추가
        etPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}