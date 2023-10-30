package com.example.matching_manager.ui.my

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.DialogEditBinding
import com.example.matching_manager.databinding.MyFragmentBinding

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editName: String? = null // editName 선언
    private var editImageUri: Uri? = null // editImageUri 선언

    private val adapter by lazy {
        MyMatchListAdapter(
            onItemClick = {
                startActivity(detailIntent(requireContext(), it))
            },
            onMenuClick = {
                val myMatchMenuBottomSheet = MyMatchMenuBottomSheet(it)

                val fragmentManager = requireActivity().supportFragmentManager
                myMatchMenuBottomSheet.show(fragmentManager, myMatchMenuBottomSheet.tag)
//                startActivity(editIntent(requireContext(), it))
            },
//            onRemoveClick = {
//                val dialog = MyDeleteDialog(it)
//                dialog.show(childFragmentManager, "deleteDialog")
//            }
        )
    }

    private var context: Context? = null
    private lateinit var dialogBinding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    companion object {
        fun newInstance() = MyFragment()
        val MY_IMAGE_POSITION = "my_image_position"
        val MY_IMAGE_MODEL = "my_image_model"
        const val PICK_IMAGE_REQUEST = 1

        const val OBJECT_DATA = "item_object"
        fun detailIntent(
            context: Context, item:
            MyMatchDataModel
        ): Intent {
            val intent = Intent(context, MyMatchDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }

        fun editIntent(context: Context, item: MyMatchDataModel): Intent {
            val intent = Intent(context, MyMatchEditActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
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

    private lateinit var myFileDialog: MyFileDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()

        myFileDialog = MyFileDialog(editName, editImageUri) { newName, newLocation, newId, newImageUri ->
            // newName, newLocation, newId, newImageUri를 이곳에서 사용할 수 있습니다.
        }


        setFragmentResultListener("imageResult") { _, result ->
            val selectedImageUri = result.getParcelable<Uri>("selectedImageUri")
            if (selectedImageUri != null) {
                //dialogBinding.ivProfile.setImageURI(selectedImageUri) // 다이얼로그 내에서의 이미지 업데이트
                //binding.ivMypageFace.setImageURI(selectedImageUri) // ivMypageFace 이미지 업데이트
                Log.d("myFragment1", "After save click: selectedImageUri = $selectedImageUri")
            }
        }
    }


    private fun initView() = with(binding) {
        progressBar.visibility = View.VISIBLE
        viewModel.fetchData(viewModel.userId)

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager


        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.fetchData(viewModel.userId)

                    val data: Intent? = result.data
                    selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        // 이미지를 선택한 후의 로직을 수행합니다.
                        dialogBinding.ivProfile.setImageURI(selectedImageUri)

                        //ivMypageFace.setImageURI(selectedImageUri)
                        //setProfileImage(selectedImageUri)
                        Log.d(
                            "MyFragment",
                            "After save click: selectedImageUri = $selectedImageUri"
                        )
                    }
                }
            }

        btnEdit.setOnClickListener {
            dialogBinding = DialogEditBinding.inflate(layoutInflater)
            val dialogView = dialogBinding.root
            //val dialogView = layoutInflater.inflate(R.layout.dialog_edit, null)
            val builder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
            val dialog = builder.create()
            dialog.show()

            val et_content_nickname = dialogBinding.etContentNickname
            val et_content_location = dialogBinding.etContentLocation
            val et_content_id = dialogBinding.etContentId
            val btn_save = dialogBinding.btnSave
            val btn_cancle = dialogBinding.btnCancle
            val iv_profile = dialogBinding.ivProfile

            btn_save.setOnClickListener {
                ivMypageFace.setImageURI(selectedImageUri)
                with(binding) {
                    val nickname = et_content_nickname.text.toString()
                    val location = et_content_location.text.toString()
                    val id = et_content_id.text.toString()

                    //setProfileImage(selectedImageUri)

                    if (nickname.isNullOrBlank() || location.isNullOrBlank() || id.isNullOrBlank()) {
                        // btn_save.isEnabled
                        Toast.makeText(
                            requireContext(),
                            "닉네임, 위치, 아이디를 입력해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        btnMypageNickname.text = nickname
                        btnMypageLocation.text = location
                        btnMypageId.text = id

                        //val newName = et_content_nickname.text.toString()
                        val newImageUri = selectedImageUri
                        dialog.dismiss()
                    }
                }

                dialog.dismiss()

        }



            btn_cancle.setOnClickListener {
                //selectedImageUri = null
                dialogBinding.ivProfile.setImageURI(null)

                dialog.dismiss()
            }


            iv_profile.setOnClickListener {

                //myFileDialog.openGallery(resultLauncher)
                openGallery()

            }
        }


        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
        //        startActivityForResult(intent, MyFragment.PICK_IMAGE_REQUEST)
    }

    private fun setProfileImage(imageUri: Uri?) {
        val result = Bundle().apply {
            putParcelable("selectedImageUri", imageUri)
        dialogBinding.ivProfile.setImageURI(imageUri)
        binding.ivMypageFace.setImageURI(imageUri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            setProfileImage(selectedImageUri)
        }
        setFragmentResult("imageResult", result)
    }

    private fun saveProfiledData(name: String, imageUri: Uri?) {
        val sharedPreferences =
            context?.getSharedPreferences("MyProfilePreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.putString("profileName", name)
        editor?.putString("profileImageUri", imageUri?.toString())

        editor?.apply()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            val selectedImageUri = data.data
//            //dialogBinding.ivProfile.setImageURI(selectedImageUri)
//            //binding.ivMypageFace.setImageURI(selectedImageUri)
//            setProfileImage(selectedImageUri)
//        }
//    }


    private fun initViewModel() = with(viewModel) {
        autoFetchData()

        list.observe(viewLifecycleOwner, Observer {
            var smoothList = 0
            adapter.submitList(it.toList())
            if(it.size > 0) smoothList = it.size - 1
            else smoothList = 1
            binding.progressBar.visibility = View.INVISIBLE
            binding.rv.smoothScrollToPosition(smoothList)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}