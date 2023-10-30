package com.example.matching_manager.ui.my

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.matching_manager.databinding.DialogEditBinding

class MyFileDialog(
    private val editName: String?,
    private val editImageUri: Uri?,
    val okCallback: (String, String, String, Uri?) -> Unit,


    ) : DialogFragment() {

    private lateinit var binding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 123
        const val PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEditBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        resultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    viewModel.fetchData(viewModel.userId)
//
//                    val data: Intent? = result.data
//                    selectedImageUri = data?.data
//                    if (selectedImageUri != null) {
//                        // 이미지를 선택한 후의 로직을 수행합니다.
//                        binding.ivProfile.setImageURI(selectedImageUri)
//
//                        //ivMypageFace.setImageURI(selectedImageUri)
//                        //setProfileImage(selectedImageUri)
//                        Log.d(
//                            "MyFragment",
//                            "After save click: selectedImageUri = $selectedImageUri"
//                        )
//                    }
//                }
//            }

        initView()


    }

    private fun initView() = with(binding) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        btnSave.setOnClickListener {
            if (etContentNickname.text.isNullOrBlank()) {
                Toast.makeText(context, "닉네임 입력하십시요!", Toast.LENGTH_SHORT).show()
            } else {
                val newName = etContentNickname.text.toString()
                val newLocation = etContentLocation.text.toString()
                val newId = etContentId.text.toString()

                val newImageUri = selectedImageUri

                // 데이터 SharedPreferences에 저장
                //saveProfiledData(newName, newImageUri)
                okCallback(
                    newName,
                    newLocation,
                    newId,
                    newImageUri
                )
                dismiss()
            }
        }
        btnCancle.setOnClickListener {
            dismiss()
        }

        ivProfile.setOnClickListener {
            //checkPermissionGalleryAndNavigateGallery()
            openGallery(resultLauncher)

        }
        editName?.let { etContentNickname.setText(it) }
        editImageUri?.let {
            selectedImageUri = it
            ivProfile.setImageURI(it)
        }


    }

    private fun checkPermissionGalleryAndNavigateGallery() {
        val context = context ?: return
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                //navigateGallery()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionContextPopup()
            }

            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )

            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한 필요")
            .setMessage("프로필 사진을 변경하려면 저장소 읽기 권한이 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun openGallery(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        this.resultLauncher.launch(intent)
        //startActivityForResult(intent, MyFragment.PICK_IMAGE_REQUEST)
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

//    fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, MyFragment.PICK_IMAGE_REQUEST)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                activity?.contentResolver?.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            selectedImageUri = imageUri
            binding.ivProfile.setImageURI(imageUri)
        }
    }


}


