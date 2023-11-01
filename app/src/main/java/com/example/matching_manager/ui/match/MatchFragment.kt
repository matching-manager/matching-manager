package com.example.matching_manager.ui.match

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchFragmentBinding
import com.example.matching_manager.ui.match.bottomsheet.MatchSortBottomSheet

class MatchFragment : Fragment() {
    private var _binding: MatchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MatchViewModel by viewModels {
        MatchViewModelFactory()
    }

    private val adapter by lazy {
        MatchListAdapter { item ->
            val matchList = viewModel.realTimeList.value ?: emptyList()
            if(matchList.any { it.matchId == item.matchId }) {
                startActivity(detailIntent(requireContext(), item))
            }
            else {
                val dialog = MatchDeletedAlertDialog()
                dialog.show(childFragmentManager, "matchDeletedAlertDialog")
            }
        }
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    companion object {
        fun newInstance() = MatchFragment()
        const val OBJECT_DATA = "item_object"
        const val ID_DATA = "item_userId"
        fun detailIntent(context: Context, item: MatchDataModel): Intent {
            val intent = Intent(context, MatchDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }

        //로그인 기능 구현 후 유저 아이디 보내줘야함
        fun writeIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, MatchWritingActivity::class.java)
            intent.putExtra(ID_DATA, userId)
            return intent
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MatchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        progressBar.visibility = View.VISIBLE

        viewModel.fetchData()

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                viewModel.fetchData()
            }
        }

        btnSort.setOnClickListener {
            val matchSortBottomSheet = MatchSortBottomSheet()

            val fragmentManager = requireActivity().supportFragmentManager
            matchSortBottomSheet.show(fragmentManager, matchSortBottomSheet.tag)
        }

        fabAdd.setOnClickListener {
            resultLauncher.launch(writeIntent(requireContext(), "testUser"))
        }
        swipeRefreshLayout.setOnRefreshListener {

            viewModel.fetchData()
            swipeRefreshLayout.isRefreshing = false
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.common_point_green)
    }

    private fun initViewModel() = with(viewModel) {
        autoFetchData()
        list.observe(viewLifecycleOwner, Observer {
            var smoothList = 0
            adapter.submitList(it.toList())
            if(it.size > 0) smoothList = it.size - 1
            else smoothList = 1
            binding.progressBar.visibility = View.INVISIBLE
            binding.rv.smoothScrollToPosition(smoothList)
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}