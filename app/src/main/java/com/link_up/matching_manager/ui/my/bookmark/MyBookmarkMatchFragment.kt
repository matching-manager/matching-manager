package com.link_up.matching_manager.ui.my.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.databinding.MyBookmarkMatchFragmentBinding
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.match.MatchDetailActivity.Companion.detailIntent
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModel
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModelFactory
import com.link_up.matching_manager.util.Utils.toMatchDataModel

class MyBookmarkMatchFragment : Fragment() {

    private var _binding: MyBookmarkMatchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyBookmarkViewModel by viewModels {
        MyBookmarkViewModelFactory(requireActivity().application)
    }

    private val adapter by lazy {
        MyBookmarkMatchListAdapter(
            onItemClick = {item ->
                startActivity(detailIntent(requireContext(), item))
            },
            onDeleteClick = {item ->
                viewModel.deleteBookmarkMatchData(item)
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
        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkMatchList.observe(viewLifecycleOwner, Observer {
            val tempList  = mutableListOf<MatchDataModel>()
            for(match in it) {
                tempList.add(match.toMatchDataModel())
            }
            adapter.submitList(tempList)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}