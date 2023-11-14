package com.link_up.matching_manager.ui.fcm.send

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.SendFcmFragmentBinding
import com.link_up.matching_manager.util.UserInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SendFcmFragment : DialogFragment() {

    private var _binding: SendFcmFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "SendFragment"
        const val INPUT_TYPE = "input_type"
        const val FCM_TOKEN = "fcm_token"
    }

    private val viewModel: SendFcmViewModel by viewModels { SendFcmViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SendFcmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {

        var sendBody: String = ""

        when (arguments?.getString(INPUT_TYPE)) {
            SendType.MERCENARY.name -> {
                sendBody = resources.getString(R.string.send_fcm_body_recruit)
                tvTitle.setText(R.string.send_message_recruit)
            }

            SendType.MATCH.name -> {
                sendBody = resources.getString(R.string.send_fcm_body_matching)
                tvTitle.setText(R.string.send_message_matching)
            }
        }
        val fcmToken = arguments?.getString(FCM_TOKEN) ?: ""
        val userName = UserInformation.userInfo.username
        val phoneNumber = UserInformation.userInfo.phoneNumber

        // Dialog의 배경을 투명으로 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnSubmit.setOnClickListener {
            runCatching {
                if (userName == null || phoneNumber == null) {
                    Toast.makeText(
                        context,
                        R.string.send_missing_user_information.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val fcmData = Payload(
                        fcmToken,
                        id = userName,
                        number = phoneNumber,
                        body = sendBody
                    )
                    pushNotification(fcmData)
                    onDestroyView()
                }
            }.onFailure { e ->
                Log.e(TAG, "Error: ${e.message}")
            }
        }
        btnCancel.setOnClickListener {
            onDestroyView()
        }
    }

    private fun pushNotification(payload: Payload) = CoroutineScope(Dispatchers.IO).launch {

        viewModel.sendData(payload)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}