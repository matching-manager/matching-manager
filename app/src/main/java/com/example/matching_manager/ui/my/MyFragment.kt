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

//        myFileDialog =
//            MyFileDialog(editName, editImageUri) { newName, newLocation, newId, newImageUri ->
//                // newName, newLocation, newId, newImageUri를 이곳에서 사용할 수 있습니다.
//            }
//
//
//        setFragmentResultListener("imageResult") { _, result ->
//            val selectedImageUri = result.getParcelable<Uri>("selectedImageUri")
//            if (selectedImageUri != null) {
//                //dialogBinding.ivProfile.setImageURI(selectedImageUri) // 다이얼로그 내에서의 이미지 업데이트
//                //binding.ivMypageFace.setImageURI(selectedImageUri) // ivMypageFace 이미지 업데이트
//                Log.d("myFragment1", "After save click: selectedImageUri = $selectedImageUri")
//            }
//        }
    }


    private fun initView() = with(binding) {
        val userData = UserInformation.userInfo
        ivPhoto.load(userData.photoUrl)
        tvUsername.text = userData.username
        tvRealName.text = userData.realName
        tvPhoneNumber.text = userData.phoneNumber
        tvEmail.text = userData.email

        cv1.setOnClickListener {
            val intent = Intent(requireContext(), MyMatchActivity::class.java)
            startActivity(intent)
        }
        cv2.setOnClickListener {
            val intent = Intent(requireContext(), MyTeamActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
