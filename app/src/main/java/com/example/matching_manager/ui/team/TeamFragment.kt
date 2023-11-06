package com.example.matching_manager.ui.team

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamFragmentBinding
import com.example.matching_manager.ui.match.MatchDeletedAlertDialog
import com.example.matching_manager.ui.match.TeamListAdapter
import com.example.matching_manager.ui.team.bottomsheet.TeamAddCategory
import com.example.matching_manager.ui.team.bottomsheet.TeamFilterCategory
import com.example.matching_manager.ui.team.viewmodel.TeamSharedViewModel
import com.example.matching_manager.ui.team.viewmodel.TeamViewModel
import com.example.matching_manager.ui.team.viewmodel.TeamViewModelFactory

class TeamFragment : Fragment() {
    private var _binding: TeamFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamViewModel by viewModels {
        TeamViewModelFactory()
    }

    private val sharedViewModel: TeamSharedViewModel by activityViewModels()

    private var game: String? = null
    private var area: String? = null

    private val listAdapter by lazy {
        TeamListAdapter(onClick = { item ->
            val matchList = viewModel.realTimeList.value ?: emptyList()
            if (matchList.any { it.teamId == item.teamId }) {
                viewModel.plusViewCount(item)
                val intent = TeamDetailActivity.newIntent(item, requireContext())
                startActivity(intent)
            } else {
                val dialog = MatchDeletedAlertDialog()
                dialog.show(childFragmentManager, "matchDeletedAlertDialog")
            }

        })
    }

    private val addContentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.fetchData(
                    binding.btnRecruitment.isChecked,
                    binding.btnApplication.isChecked,
                    game,
                    area
                )
            }

            binding.apply {
                btnRecruitment.isChecked = false
                btnApplication.isChecked = false
            }
        }


    companion object {
        fun newInstance() = TeamFragment()
        const val FRAGMENT_REQUEST_KEY = "request_key"
        const val CATEGORY_REQUEST_KEY = "category_key"
        const val FRAGMENT_RETURN_TYPE = "fragment_return_type"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = TeamFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }


    private fun initView() = with(binding) {

        progressBar.visibility = View.VISIBLE

        viewModel.fetchData(btnRecruitment.isChecked, btnApplication.isChecked, game, area)

        recyclerview.adapter = listAdapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerview.layoutManager = manager

        btnApplication.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvFilter.text = "전체 글"
                btnRecruitment.isChecked = false
                viewModel.filterApplicationItems() // 용병신청
            } else if (!btnRecruitment.isChecked) {
                viewModel.clearFilter() // 둘 다 체크 안되어 있을 때만 필터링 제거
                tvFilter.text = "전체 글"
                game = null
                area = null
            }
        }

        btnRecruitment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvFilter.text = "전체 글"
                btnApplication.isChecked = false
                viewModel.filterRecruitmentItems() // 용병모집
            } else if (!btnApplication.isChecked) {
                viewModel.clearFilter() // 둘 다 체크 안되어 있을 때만 필터링 제거
                tvFilter.text = "전체 글"
                game = null
                area = null
            }
        }


        //add btn
        fabAdd.setOnClickListener {
            val teamAddCategory = TeamAddCategory()
            teamAddCategory.show(childFragmentManager, teamAddCategory.tag)
            //프래그먼트의 childFragmentManager를 쓰면 같은 라이프사이클을 사용 해야함
            childFragmentManager.setFragmentResultListener(
                FRAGMENT_REQUEST_KEY, viewLifecycleOwner
            ) { key, bundle ->
                val result = bundle.getString(FRAGMENT_RETURN_TYPE)

                when (result) {
                    TeamAddCategory.RETURN_TYPE_RECRUITMENT -> {
                        val intent = TeamWritingActivity.newIntentForAddRecruit(
                            requireContext(),
                            TeamAddType.RECRUIT.name
                        )
                        addContentLauncher.launch(intent)
                    }

                    TeamAddCategory.RETURN_TYPE_APPLICATION -> {
                        val intent = TeamWritingActivity.newIntentForAddApplication(
                            requireContext(),
                            TeamAddType.APPLICATION.name
                        )
                        addContentLauncher.launch(intent)
                    }
                }

            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchData(
                area = area,
                game = game,
                isRecruitmentChecked = btnRecruitment.isChecked,
                isApplicationChecked = btnApplication.isChecked
            )
            swipeRefreshLayout.isRefreshing = false
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.common_point_green)

        //filtr btn
        btnFilter.setOnClickListener {
            val teamFilterCategory = TeamFilterCategory()
            val fragmentManager = requireActivity().supportFragmentManager
            teamFilterCategory.show(fragmentManager, teamFilterCategory.tag)

            setFragmentResultListener(CATEGORY_REQUEST_KEY) { _, bundle ->
                //결과 값을 받는곳입니다.
                game = bundle.getString(TeamFilterCategory.SELECTED_GAME)
                area = bundle.getString(TeamFilterCategory.SELECTED_AREA)

                //선택한 게임과 지역에 따라 아이템을 필터링합니다.
                viewModel.filterItems(
                    area = area,
                    game = game,
                    isRecruitmentChecked = btnRecruitment.isChecked,
                    isApplicationChecked = btnApplication.isChecked
                )
            }
        }
    }

    //viewmodel init
    private fun initViewModel() = with(binding) {
        with(viewModel) {
            autoFetchData()
            list.observe(viewLifecycleOwner, Observer {
                var smoothList = 0
                listAdapter.submitList(it.toList())
                if (it.size > 0) smoothList = it.size - 1
                else smoothList = 1
                binding.progressBar.visibility = View.INVISIBLE
                binding.recyclerview.smoothScrollToPosition(smoothList)

                if (it != null) {
                    if (it.isEmpty()) {
                        tvEmpty.visibility = (View.VISIBLE)
                        listAdapter.submitList(it)
                    } else {
                        tvEmpty.visibility = (View.INVISIBLE)
                        listAdapter.submitList(it)
                    }
                }
            })
        }
        with(sharedViewModel) {
            filter.observe(viewLifecycleOwner, Observer { (area, game) ->
                val filter = kotlin.String.format("%s / %s", area, game)
                val areaFilter = kotlin.String.format("모든 지역/%s", game)
                val gameFilter = kotlin.String.format("%s/모든 경기", area)

                if (area.contains("선택") && game.contains("선택")) {
                    tvFilter.text = "전체 글"
                    tvFilter.visibility = (View.VISIBLE)
                } else if (area.contains("선택")) {
                    tvFilter.text = areaFilter
                    tvFilter.visibility = (View.VISIBLE)
                } else if (game.contains("선택")) {
                    tvFilter.text = gameFilter
                    tvFilter.visibility = (View.VISIBLE)
                } else {
                    tvFilter.text = filter
                    tvFilter.visibility = (View.VISIBLE)
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}