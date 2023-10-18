package com.example.matching_manager.ui.team

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.TeamFragmentBinding
import com.example.matching_manager.ui.match.TeamListAdapter
import com.example.matching_manager.ui.team.bottomsheet.TeamAddCategory
import com.example.matching_manager.ui.team.bottomsheet.TeamFilterCategory
import com.example.matching_manager.ui.team.view.TeamViewModel

class TeamFragment : Fragment() {
    private var _binding: TeamFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamViewModel by lazy {
        ViewModelProvider(this)[TeamViewModel::class.java]
    }

    private val listAdapter by lazy {
        TeamListAdapter { item ->
            val intent = TeamDetailActivity.newIntent(item, requireContext())
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() = TeamFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = TeamFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {
        recyclerview.adapter = listAdapter

        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        //add btn
        fabAdd.setOnClickListener {
            val teamAddCategory = TeamAddCategory()

            val fragmentManager = requireActivity().supportFragmentManager
            teamAddCategory.show(fragmentManager, teamAddCategory.tag)
        }
        //filtr btn
        btnFilter.setOnClickListener {
            val teamFilterCategory = TeamFilterCategory()

            val fragmentManager = requireActivity().supportFragmentManager
            teamFilterCategory.show(fragmentManager, teamFilterCategory.tag)

        }

    }

    //viewmodel init
    private fun initViewModel() = with(viewModel) {
        list.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}