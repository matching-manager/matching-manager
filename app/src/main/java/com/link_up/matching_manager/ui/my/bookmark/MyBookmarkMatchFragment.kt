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
import com.link_up.matching_manager.databinding.MyBookmarkMatchFragmentBinding
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.match.MatchDetailActivity.Companion.detailIntent
import com.link_up.matching_manager.ui.my.match.MyMatchViewModelFactory
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.google.gson.Gson

class MyBookmarkMatchFragment : Fragment() {

    private var _binding: MyBookmarkMatchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getMatchBookmark()
        }
    }


    private val adapter by lazy {
        MyBookmarkMatchListAdapter(
            onItemClick = {item ->
                resultLauncher.launch(detailIntent(requireContext(), item))
            },
            onDeleteClick = {item ->
                val bookmarkPref = requireActivity().getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
                val editor = bookmarkPref.edit()
                editor.remove("Match_${item.matchId}")
                editor.apply()

                getMatchBookmark()
            }

        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyBookmarkMatchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        getMatchBookmark()

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun getMatchBookmark() {
        val bookmarkPref = requireActivity().getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val gson = Gson()
        val keys = bookmarkPref.all.keys
        val dataList = mutableListOf<MatchDataModel>()
        for (key in keys) {
            if (key.startsWith("Match_")) {
                val json = bookmarkPref.getString(key, null)
                if (!json.isNullOrBlank()) {
                    val data = gson.fromJson(json, MatchDataModel::class.java)
                    dataList.add(data)
                }
            }
        }
        viewModel.addBookmarkMatchLiveData(dataList)
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkMatchList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}