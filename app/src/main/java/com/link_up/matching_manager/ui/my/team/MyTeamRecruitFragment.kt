package com.link_up.matching_manager.ui.my.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.databinding.MyTeamRecruitFragmentBinding
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.link_up.matching_manager.ui.my.team.MyTeamRecruitDetailActivity.Companion.detailIntent
import com.link_up.matching_manager.ui.my.my.MyViewModelFactory
import com.link_up.matching_manager.util.UserInformation

class MyTeamRecruitFragment : Fragment() {

    private var _binding: MyTeamRecruitFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyViewModelFactory()
    }

    private val adapter by lazy {
        MyTeamRecruitListAdapter(
            onItemClick = {
                startActivity(detailIntent(requireContext(), it))
            },
            onMenuClick = {
                val myTeamRecruitMenuBottomSheet = MyTeamRecruitMenuBottomSheet(it)

                myTeamRecruitMenuBottomSheet.show(parentFragmentManager, myTeamRecruitMenuBottomSheet.tag)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyTeamRecruitFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding){
        progressBar.visibility = View.VISIBLE
        viewModel.fetchRecruitData(UserInformation.userInfo.uid!!)

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun initViewModel() = with(viewModel) {
        autoFetchRecruitData()

        recruitList.observe(viewLifecycleOwner, Observer {
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