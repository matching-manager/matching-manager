package com.example.matching_manager.ui.my

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import coil.load
import com.example.matching_manager.databinding.DialogEditBinding
import com.example.matching_manager.databinding.MyFragmentBinding
import com.example.matching_manager.ui.signin.UserInformation
import com.google.firebase.auth.FirebaseAuth

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editName: String? = null // editName 선언
    private var editImageUri: Uri? = null // editImageUri 선언


    private var context: Context? = null
    private lateinit var dialogBinding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    companion object {
        fun newInstance() = MyFragment()
        const val OBJECT_DATA = "item_object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = MyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var myFileDialog: MyFileDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }


    private fun initView() = with(binding) {
        val userData = UserInformation.userInfo
        ivPhoto.load(userData.photoUrl)
        tvUsername.text = userData.username
        tvRealName.text = userData.realName
        tvPhoneNumber.text = userData.phoneNumber
        tvEmail.text = userData.email

        layoutMatch.setOnClickListener {
            val intent = Intent(requireContext(), MyMatchActivity::class.java)
            startActivity(intent)
        }
        layoutTeam.setOnClickListener {
            val intent = Intent(requireContext(), MyTeamActivity::class.java)
            startActivity(intent)
        }
        layoutBookmark.setOnClickListener {
            // 현준님 여기에 관심목록 저장하시면 됩니다~
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
