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
import com.link_up.matching_manager.ui.my.match.MyMatchViewModelFactory
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.link_up.matching_manager.ui.team.TeamDetailActivity
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.gson.Gson

class MyBookmarkApplicationFragment : Fragment() {

    private var _binding: MyBookmarkApplicationFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getApplicationBookmark()
        }
    }

    private val adapter by lazy {
        MyBookmarkApplicationListAdapter(
            onItemClick = {item ->
                resultLauncher.launch(TeamDetailActivity.newIntent(item, requireContext()))
            },
            onDeleteClick = {item ->
                val bookmarkPref = requireActivity().getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
                val editor = bookmarkPref.edit()
                editor.remove("Application_${item.teamId}")
                editor.apply()

                getApplicationBookmark()
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
        getApplicationBookmark()

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun getApplicationBookmark() {
        val bookmarkPref = requireActivity().getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val gson = Gson()
        val keys = bookmarkPref.all.keys
        val dataList = mutableListOf<TeamItem.ApplicationItem>()
        for (key in keys) {
            if (key.startsWith("Application_")) {
                val json = bookmarkPref.getString(key, null)
                if (!json.isNullOrBlank()) {
                    val data = gson.fromJson(json, BookmarkApplicationDataModel::class.java)
                    dataList.add(bookmarkApplicationToTeamItem(data))
                }
            }
        }
        viewModel.addBookmarkApplicationLiveData(dataList)
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkApplicationList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun bookmarkApplicationToTeamItem(item : BookmarkApplicationDataModel) : TeamItem.ApplicationItem {
        return TeamItem.ApplicationItem(
            type = item.type,
            teamId = item.teamId,
            userId = item.userId,
            nickname = item.nickname,
            userImg = item.userImg,
            userEmail = item.userEmail,
            phoneNum = item.phoneNum,
            fcmToken = item.fcmToken,
            description = item.description,
            gender = item.gender,
            chatCount = item.chatCount,
            level = item.level,
            playerNum = item.playerNum,
            postImg = item.postImg,
            schedule = item.schedule,
            uploadTime = item.uploadTime,
            viewCount = item.viewCount,
            game = item.game,
            area = item.area,
            age = item.age
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}