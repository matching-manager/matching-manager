package com.example.matching_manager.ui.team

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.TeamFragmentBinding
import com.example.matching_manager.databinding.TeamMyteamFragmentBinding
import com.example.matching_manager.ui.main.MainActivity
import com.example.matching_manager.ui.match.MatchCategory
import com.example.matching_manager.ui.match.MatchDetailActivity
import com.example.matching_manager.ui.match.MatchFragment
import com.example.matching_manager.ui.match.TeamListAdapter
import com.example.matching_manager.ui.team.view.TeamViewModel
import com.google.android.material.tabs.TabLayoutMediator

class TeamFragment : Fragment() {
    private var _binding: TeamMyteamFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamViewModel by lazy { ViewModelProvider(this)[TeamViewModel::class.java]
    }

    private val listAdapter by lazy {
        TeamListAdapter{ item ->
            val intent = Intent(requireContext(), TeamDetailActivity::class.java)
            intent.putExtra(MatchFragment.OBJECT_DATA, item)
            startActivity(intent)
        }
    }


    companion object {
        fun newInstance() = TeamFragment()
        const val OBJECT_DATA = "item_object"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TeamMyteamFragmentBinding.inflate(inflater, container, false)
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
            val matchCategory = MatchCategory()

            val fragmentManager = requireActivity().supportFragmentManager
            matchCategory.show(fragmentManager, matchCategory.tag)
        }


    }

    //viewmodel init
    private fun initViewModel()= with(viewModel) {
        list.observe(viewLifecycleOwner){
            listAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}