package com.example.matching_manager.ui.match

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.MatchFragmentBinding

class MatchFragment : Fragment() {
    private var _binding: MatchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MatchViewModel by viewModels {
        MatchViewModelFactory()
    }

    private val adapter by lazy {
        MatchListAdapter { item ->
            startActivity(newIntent(requireContext(), item))
        }
    }

    companion object {
        fun newInstance() = MatchFragment()
        const val OBJECT_DATA = "item_object"
        const val ID_DATA = "item_userId"
        fun newIntent(context: Context, item: MatchDataModel): Intent {
            val intent = Intent(context, MatchDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
        fun secondIntent(context: Context, userId : String): Intent {
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

        viewModel.updateList()

        rv.adapter = adapter

        rv.layoutManager = LinearLayoutManager(requireContext())


        btnCategory.setOnClickListener {
            val matchCategory = MatchCategory()

            val fragmentManager = requireActivity().supportFragmentManager
            matchCategory.show(fragmentManager, matchCategory.tag)
        }

        fabAdd.setOnClickListener {
            startActivity(secondIntent(requireContext(), "Test"))
        }
    }

    private fun initViewModel() = with(viewModel) {
        list.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}