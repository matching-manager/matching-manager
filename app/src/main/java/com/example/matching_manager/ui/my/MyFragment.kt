package com.example.matching_manager.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyFragmentBinding
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.example.matching_manager.ui.match.MatchFragment

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!
    private var context: Context? = null

    companion object {
        fun newInstance() = MyFragment()
        val MY_IMAGE_POSITION = "my_image_position"
        val MY_IMAGE_MODEL = "my_image_model"
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

    private fun initView() = with(binding){

    }


    private fun showProfileDialog() = with(binding){
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