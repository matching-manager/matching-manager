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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.R
import com.example.matching_manager.databinding.DialogEditBinding
import com.example.matching_manager.databinding.MyFragmentBinding
import kotlinx.coroutines.launch

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val adapter by lazy {
        MyMatchListAdapter(
            onItemClick = {
                startActivity(detailIntent(requireContext(), it))
            },
            onEditClick = { item, position ->
                resultLauncher.launch(editIntent(requireContext(), item))
            },
            onRemoveClick = { item, position ->
                val dialog = MyDeleteDialog(item)
                dialog.setOnDismissListener(object : MyDeleteDialog.OnDialogDismissListener {
                    override fun onDismiss() {
                        viewModel.fetchData(viewModel.userId)
                    }
                })
                dialog.show(childFragmentManager, "deleteDialog")
            })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
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

    private fun initViewModel() = with(viewModel) {
        list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toList())
            binding.progressBar.visibility = View.INVISIBLE
            Log.d("listData", "${it.size}")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}