package com.example.matching_manager.ui.fcm.send

import android.content.Context
import android.content.Intent
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
import com.example.matching_manager.databinding.SendFcmFragmentBinding
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
//        const val TOKEN1 = "dqhHfR6YSE6Ar5h1r6FKqb:APA91bEkPVynEwTbO4iPACeyUxFzsU-mP-9WQSeDZKMUxwI8mjpFy4MdySL9P9Rn5JLXiz4QWB0JvPi0FFWX5PLpOzysf5-HD8ujfIcDx471tEBV3BzGg__l9MwCO13UUjjg5aabzSun"
//        const val TOKEN2 = "c72G5pn1RWmv76uCpW7so3:APA91bERbSy1FBaAjIje9p_eBwG8zKd5ZxENue4zQp2lRanmqy5VFJ0Iw3yepVJ7qhZVcFKCApo4kUu8IRcqi4DIC0yIJUmZVVI5mHvIAHws7XWXgfYBqk-GNOhgD7hym0CTLxL0HNoe"
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

        var sendBody :String = ""

        when(arguments?.getString(INPUT_TYPE)){
            SendType.MERCENARY.name-> sendBody = "용병 모집 신청입니다."
            SendType.MATCH.name -> sendBody = "경기 매칭 신청입니다."
        }
        val fcmToken = arguments?.getString(FCM_TOKEN) ?: ""

        // Dialog의 배경을 투명으로 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnSubmit.setOnClickListener {
            runCatching {
                if (etId.text.isEmpty() || etPhoneNumber.text.isEmpty()) {
                    Toast.makeText(context, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show()
                } else {
                    val fcmData = Payload(
                        fcmToken,
                        id = etId.text.toString(),
                        number = etPhoneNumber.text.toString(),
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
//        FcmRetrofitClient.api.postNotification(
//            payload = payload
//        )
        viewModel.sendData(payload)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}