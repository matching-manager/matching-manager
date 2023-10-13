package com.example.matching_manager.ui.match

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchFragmentBinding

class MatchFragment : Fragment() {
    private var _binding: MatchFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        fun newInstance() = MatchFragment()
        const val OBJECT_DATA = "item_object"
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
    }

    private fun initView() = with(binding) {
        val dummyData = mutableListOf<MatchData>()
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
        dummyData.add(MatchData(R.drawable.sonny, "팀 매칭", "11:11 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))


        val adapter = MatchListAdapter { item ->
            val intent = Intent(requireContext(), MatchDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            startActivity(intent)
        }
        rv.adapter = adapter

        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter.submitList(dummyData)

        btnCategory.setOnClickListener {
            val matchCategory = MatchCategory()

            val fragmentManager = requireActivity().supportFragmentManager
            matchCategory.show(fragmentManager, matchCategory.tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}