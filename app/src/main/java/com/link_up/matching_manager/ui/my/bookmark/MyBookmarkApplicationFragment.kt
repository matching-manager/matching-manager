package com.link_up.matching_manager.ui.my.bookmark

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.databinding.MyBookmarkApplicationFragmentBinding
import com.link_up.matching_manager.ui.my.my.MyPostViewModelFactory
import com.link_up.matching_manager.ui.my.my.MyPostViewModel
import com.link_up.matching_manager.ui.team.TeamDetailActivity
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.gson.Gson
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModel
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModelFactory
import com.link_up.matching_manager.ui.team.TeamDetailActivity.Companion.newIntent
import com.link_up.matching_manager.util.Utils.toApplication

class MyBookmarkApplicationFragment : Fragment() {

    private var _binding: MyBookmarkApplicationFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyBookmarkViewModel by viewModels {
        MyBookmarkViewModelFactory(requireActivity().application)
    }

    private val adapter by lazy {
        MyBookmarkApplicationListAdapter(
            onItemClick = {item ->
                startActivity(newIntent(item, requireContext()))
            },
            onDeleteClick = {item ->
                viewModel.deleteBookmarkApplicationData(item)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyBookmarkApplicationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkApplicationList.observe(viewLifecycleOwner, Observer {
            val tempList = mutableListOf<TeamItem.ApplicationItem>()
            for(application in it) {
                tempList.add(application.toApplication())
            }
            adapter.submitList(tempList)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}