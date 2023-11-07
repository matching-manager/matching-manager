package com.example.matching_manager.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.MyTeamApplicationFragmentBinding
import com.example.matching_manager.ui.my.MyTeamApplicationDetailActivity.Companion.detailIntent
import com.example.matching_manager.ui.signin.UserInformation
import com.example.matching_manager.ui.team.TeamItem

class MyTeamApplicationFragment : Fragment() {

    private var _binding: MyTeamApplicationFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val adapter by lazy {
        MyTeamApplicationListAdapter(
            onItemClick = {
                startActivity(detailIntent(requireContext(), it))
            },
            onMenuClick = {
                val myTeamApplicationMenuBottomSheet = MyTeamApplicationMenuBottomSheet(it)

                myTeamApplicationMenuBottomSheet.show(parentFragmentManager, myTeamApplicationMenuBottomSheet.tag)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyTeamApplicationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding){
        progressBar.visibility = View.VISIBLE
        viewModel.fetchApplicationData(UserInformation.userInfo.uid!!)

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun initViewModel() = with(viewModel) {
        autoFetchApplicationData()

        applicationList.observe(viewLifecycleOwner, Observer {
            var smoothList = 0
            adapter.submitList(it.toList())
            if (it.size > 0) smoothList = it.size - 1
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