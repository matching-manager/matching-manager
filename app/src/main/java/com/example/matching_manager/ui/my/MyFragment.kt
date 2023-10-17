package com.example.matching_manager.ui.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.matching_manager.R
import com.example.matching_manager.databinding.DialogEditBinding
import com.example.matching_manager.databinding.MyFragmentBinding
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.example.matching_manager.ui.home.HomeFragment
import com.example.matching_manager.ui.match.MatchFragment
import com.example.matching_manager.ui.my.MyFragment.Companion.PICK_IMAGE_REQUEST

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!
    private var context: Context? = null
    private lateinit var dialogBinding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    companion object {
        fun newInstance() = MyFragment()
        val MY_IMAGE_POSITION = "my_image_position"
        val MY_IMAGE_MODEL = "my_image_model"
        const val PICK_IMAGE_REQUEST = 1
    }

//    private val listAdapter by lazy {
//        MyAdapter { position, image ->
//            val intent = Intent(context, Image)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }


    private fun initView() = with(binding) {
        btnEdit.setOnClickListener {
            dialogBinding = DialogEditBinding.inflate(layoutInflater)
            val dialogView = dialogBinding.root
            //val dialogView = layoutInflater.inflate(R.layout.dialog_edit, null)
            val builder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
            val dialog = builder.create()
            dialog.show()

            val et_content_nickname = dialogView.findViewById<EditText>(R.id.et_content_nickname)
            val et_content_location = dialogView.findViewById<EditText>(R.id.et_content_location)
            val et_content_id = dialogView.findViewById<EditText>(R.id.et_content_id)
            val btn_save = dialogView.findViewById<Button>(R.id.btn_save)
            val btn_cancle = dialogView.findViewById<Button>(R.id.btn_cancle)
            val iv_profile = dialogView.findViewById<ImageView>(R.id.iv_profile)

            btn_save.setOnClickListener {
                with(binding) {
                    val nickname = et_content_nickname.text.toString()
                    val location = et_content_location.text.toString()
                    val id = et_content_id.text.toString()

                    selectedImageUri?.let { imageUri ->
                        ivMypageFace.setImageURI(imageUri)
                    }

                    btnMypageNickname.text = nickname
                    btnMypageLocation.text = location
                    btnMypageId.text = id

                    dialog.dismiss()
                }
                    //setProfileImage(selectedImageUri)


//                if (et_content_nickname.text.isNullOrBlank() && et_content_location.text.isNullOrBlank() && et_content_id.text.isNullOrBlank()) {
//                    Toast.makeText(context, "입력하십시요!", Toast.LENGTH_SHORT).show()
//                }  else {
//                    val newName = et_content_nickname.text.toString()
//                    val newImageUri = selectedImageUri
//
//                    // 데이터 SharedPreferences에 저장
//                    //saveProfiledData(newName, newImageUri)
//                    //okCallback(etContent.text.toString(), selectedImageUri)
//                }

            }

            btn_cancle.setOnClickListener {
                dialog.dismiss()
            }

//            btnEdit.setOnClickListener{
//                openGallery()
//            }

            iv_profile.setOnClickListener {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun setProfileImage(imageUri: Uri?) {
        dialogBinding.ivProfile.setImageURI(imageUri)
        binding.ivMypageFace.setImageURI(imageUri)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
                //dialogBinding.ivProfile.setImageURI(selectedImageUri)
           //binding.ivMypageFace.setImageURI(selectedImageUri)
            setProfileImage(selectedImageUri)
        }
    }

    private fun showProfileDialog() = with(binding) {
        //MyFileDialog(
        //viewModel.profileName.value,
        //viewModel.profileImageUri.value
        //) { newName, newImageUri ->
        //viewModel.setProfile(newName, newImageUri)
        //}.show(parentFragmentManager, "MyFileDialog")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}