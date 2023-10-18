package com.example.matching_manager.ui.match

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.MatchFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchFragment : Fragment() {
    private var _binding: MatchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MatchViewModel by viewModels {
        MatchViewModelFactory()
    }

    private val adapter by lazy {
        MatchListAdapter { item ->
            startActivity(detailIntent(requireContext(), item))
        }
    }

    companion object {
        fun newInstance() = MatchFragment()
        const val OBJECT_DATA = "item_object"
        const val ID_DATA = "item_userId"
        fun detailIntent(context: Context, item: MatchDataModel): Intent {
            val intent = Intent(context, MatchDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }

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


        lifecycleScope.launch {
            viewModel.fetchData()
        }

        rv.adapter = adapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager


        btnCategory.setOnClickListener {
            val matchCategory = MatchCategory()

            val fragmentManager = requireActivity().supportFragmentManager
            matchCategory.show(fragmentManager, matchCategory.tag)
        }

        fabAdd.setOnClickListener {
            startActivity(writeIntent(requireContext(), "Test"))
        }
    }

    private fun initViewModel() = with(viewModel) {
        list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toList())
            Log.d("listData", "${it.size}")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}