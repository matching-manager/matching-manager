package com.example.matching_manager.ui.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.matching_manager.R
import com.example.matching_manager.databinding.SignUpActivityBinding
import com.example.matching_manager.ui.fcm.FcmActivity

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { SignUpActivityBinding.inflate(layoutInflater) }

    private val sharedViewModel: SignInSharedViewModel by viewModels {
        SignInViewModelFactory(
            context = baseContext
        )
    }

    companion object {
        const val TAG = "SignUpActivity"

        fun newIntent(context: Context) = Intent(
            context,
            SignUpActivity::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        // 연락처 입력 시 하이픈 자동 추가
        etPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())


        btnSignUp.setOnClickListener {
            when {
                etPhoneNumber.length() != 13 || !etPhoneNumber.text.startsWith("010") && etUserName.length() < 2 -> {
                    userNameException()
                    userPhoneNumberException()
                }

                etPhoneNumber.length() != 13 || !etPhoneNumber.text.startsWith("010") -> {
                    userPhoneNumberException()
                }

                etUserName.length() < 2 -> {
                    userNameException()
                }

                else -> {
                    sharedViewModel.addUserInfo(
                        etUserName.text.toString(),
                        etPhoneNumber.text.toString()
                    )
                    startActivity(FcmActivity.newIntent(this@SignUpActivity))
                }
            }
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun userNameException() = with(binding) {
        Log.d(TAG, "UserName exception")
        ivWarningUsername.isVisible = true
        tvUsernameDescription.setTextColor(resources.getColor(R.color.signup_exception_red))
        tvUsernameDescription.text = "2자리 이상의 UserName을 입력해주세요!"
        etUserName.isFocusable = false
        etUserName.isSelected = true
    }

    private fun userPhoneNumberException() = with(binding) {
        Log.d(TAG, "phoneNumber exception")
        ivWarningPhoneNumber.isVisible = true
        tvPhoneNumberDescription.setTextColor(resources.getColor(R.color.signup_exception_red))
        tvPhoneNumberDescription.text = "010으로 시작하는 11자리 번호를 입력해주세요!"
        etPhoneNumber.isFocusable = false
        etPhoneNumber.isSelected = true
    }
}