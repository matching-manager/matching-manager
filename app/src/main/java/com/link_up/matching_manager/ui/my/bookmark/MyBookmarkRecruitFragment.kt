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
import com.link_up.matching_manager.databinding.MyBookmarkRecruitFragmentBinding
import com.link_up.matching_manager.ui.my.match.MyMatchViewModelFactory
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.link_up.matching_manager.ui.team.TeamDetailActivity
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.gson.Gson

class MyBookmarkRecruitFragment : Fragment() {

    private var _binding: MyBookmarkRecruitFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getRecruitBookmark()
        }
    }

    private val adapter by lazy {
        MyBookmarkRecruitListAdapter(
            onItemClick = {item ->
                resultLauncher.launch(TeamDetailActivity.newIntent(item, requireContext()))
            },
            onDeleteClick = {item ->
                val bookmarkPref = requireActivity().getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
                val editor = bookmarkPref.edit()
                editor.remove("Recruit_${item.teamId}")
                editor.apply()

                getRecruitBookmark()

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
        getRecruitBookmark()

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun getRecruitBookmark() {
        val bookmarkPref = requireActivity().getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val gson = Gson()
        val keys = bookmarkPref.all.keys
        val dataList = mutableListOf<TeamItem.RecruitmentItem>()
        for (key in keys) {
            if (key.startsWith("Recruit")) {
                val json = bookmarkPref.getString(key, null)
                if (!json.isNullOrBlank()) {
                    val data = gson.fromJson(json, TeamItem.RecruitmentItem::class.java)
                    dataList.add(data)
                }
            }
        }
        viewModel.addBookmarkRecruitLiveData(dataList)
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkRecruitList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}