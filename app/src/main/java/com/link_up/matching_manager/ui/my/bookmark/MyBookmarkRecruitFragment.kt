package com.link_up.matching_manager.ui.my.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.databinding.MyBookmarkRecruitFragmentBinding
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModel
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModelFactory
import com.link_up.matching_manager.ui.team.TeamDetailActivity.Companion.newIntent
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.util.Utils.toRecruitment

class MyBookmarkRecruitFragment : Fragment() {

    private var _binding: MyBookmarkRecruitFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyBookmarkViewModel by viewModels {
        MyBookmarkViewModelFactory(requireActivity().application)
    }

    private val adapter by lazy {
        MyBookmarkRecruitListAdapter(
            onItemClick = {item ->
                startActivity(newIntent(item, requireContext()))
            },
            onDeleteClick = {item ->
                viewModel.deleteBookmarkRecruitmentData(item)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyBookmarkRecruitFragmentBinding.inflate(inflater, container, false)
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
        bookmarkRecruitList.observe(viewLifecycleOwner, Observer {
            val tempList = mutableListOf<TeamItem.RecruitmentItem>()
            for(recruitment in it) {
                tempList.add(recruitment.toRecruitment())
            }
            adapter.submitList(tempList)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}