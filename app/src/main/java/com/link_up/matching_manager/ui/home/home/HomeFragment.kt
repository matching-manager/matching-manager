package com.link_up.matching_manager.ui.home.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.HomeFragmentBinding
import com.link_up.matching_manager.ui.home.alarm.AlarmActivity
import com.link_up.matching_manager.ui.home.arena.ArenaActivity
import com.link_up.matching_manager.ui.main.MainActivity
import com.link_up.matching_manager.ui.match.MatchDetailActivity

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val announcementListAdapter: HomeAnnouncementListAdapter by lazy {
        HomeAnnouncementListAdapter(
            onClick = {
                viewModel.announcementData(it)
                // TODO : 공지사항 모델 클릭 시 이벤트 처리
                AnnouncementFragment().apply {
                    // arguments = Bundle().apply { } -> 넘겨줄 값을 Bundle or ViewModel을 통해서 넘겨준다.
                }.show(requireActivity().supportFragmentManager, "SampleDialog")
            }
        )
    }

    private val matchListAdapter: HomeMatchListAdapter by lazy {
        HomeMatchListAdapter(
            onClick = {item ->
                startActivity(MatchDetailActivity.detailIntent(requireContext(), item))
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        fetchMatchData()
        fetchAnnounceData()
        // viewModel 리스트 변경시 화면에 출력
        announcementList.observe(viewLifecycleOwner, Observer {
            announcementListAdapter.submitList(it)
        })
        matchList.observe(viewLifecycleOwner, Observer {
            matchListAdapter.submitList(it)
        })
    }

    private fun initView() = with(binding) {
        rvAnnouncement.adapter = announcementListAdapter
        rvMatch.adapter = matchListAdapter

        rvAnnouncement.isVerticalScrollBarEnabled = false
        rvMatch.isVerticalScrollBarEnabled = false

        val announcementListManager = LinearLayoutManager(requireContext())
        announcementListManager.reverseLayout = true
        announcementListManager.stackFromEnd = true

        val matchListManager = LinearLayoutManager(requireContext())
        matchListManager.reverseLayout = true
        matchListManager.stackFromEnd = true

        rvAnnouncement.layoutManager = announcementListManager
        rvMatch.layoutManager = matchListManager

        btnMoreInformation.setOnClickListener {
            (activity as? MainActivity)?.navigateToMatch()
        }
        btnArena.setOnClickListener {
            val intent = Intent(requireContext(), ArenaActivity::class.java)
            startActivity(intent)
        }
        btnHomeNoti.setOnClickListener {
            val intent = Intent(requireContext(), AlarmActivity::class.java)
            startActivity(intent)
        }
        tvMoreAnnouncement.setOnClickListener {
            val intent = Intent(requireContext(), AnnouncementActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}