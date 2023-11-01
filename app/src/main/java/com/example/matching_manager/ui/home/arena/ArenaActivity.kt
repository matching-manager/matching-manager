package com.example.matching_manager.ui.home.arena

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.ArenaActivityBinding
import com.example.matching_manager.ui.fcm.send.SendFcmFragment
import com.example.matching_manager.ui.home.arena.bottomsheet.ArenaFilterCategory
import com.example.matching_manager.ui.home.arena.detail_dialog.ArenaDetailFragment
import com.example.matching_manager.ui.team.TeamAddType
import com.example.matching_manager.ui.team.TeamFragment
import com.example.matching_manager.ui.team.TeamItem
import com.example.matching_manager.ui.team.TeamWritingActivity
import com.example.matching_manager.ui.team.bottomsheet.TeamAddCategory

class ArenaActivity : AppCompatActivity() {

    private val binding by lazy { ArenaActivityBinding.inflate(layoutInflater) }

    private val viewModel: ArenaViewModel by viewModels { ArenaViewModelFactory() }

    private val listAdapter: ArenaListAdapter by lazy {
        ArenaListAdapter(
            onClick = { arenaModel ->
                viewModel.updateItem(item = arenaModel)
                ArenaDetailFragment().show(
                    supportFragmentManager, "SampleDialog"
                )
            }
        )
    }

    // 데이터 추가
    private val filterLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val arenaModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(
                        ArenaFilterCategory.SELECTED_AREA,
                        ArenaModel::class.java
                    )
                } else {
                    result.data?.getParcelableExtra(
                        ArenaFilterCategory.SELECTED_AREA,
                    )
                }

            }
        }

    private

    companion object {
        const val ARENA_FILTER = "arena_filter"
        var CHECK_FIRST_FILTER = true
        fun newIntent(
            context: Context,
        ) = Intent(context, ArenaActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        rvArena.adapter = listAdapter
        //색지정하기

        btnBack.setOnClickListener {
            onBackPressed()
        }
        btnFutsal.setOnClickListener {
            searchArena("풋살")
        }
        btnSoccer.setOnClickListener {
            searchArena("축구장")
        }
        btnBowling.setOnClickListener {
            searchArena("볼링장")
        }
        btnBasketball.setOnClickListener {
            searchArena("농구장")
        }
        btnBadminton.setOnClickListener {
            searchArena("배드민턴장")
        }
        btnFilter.setOnClickListener {
            val arenaFilterCategory = ArenaFilterCategory()

            arenaFilterCategory.setOnFilterSelectedListener(object :
                ArenaFilterCategory.OnFilterSelectedListener {
                override fun onFilterSelected(selectedArea: String?) {
                    // 선택한 지역값을 받아왔으므로, 이를 ViewModel에 전달합니다.
                    selectedArea?.let { viewModel.setFilterArea(selectedArea) }
                }
            })

            arenaFilterCategory.show(supportFragmentManager, "ArenaFilterCategory")
        }
    }

    private fun searchArena(text: String) {
        viewModel.searchArena(text, this)
    }

    private fun initModel() = with(viewModel) {
        list.observe(this@ArenaActivity, Observer {
            if (it.isEmpty()) {
                binding.tvEmpty.visibility = (View.VISIBLE)
                listAdapter.submitList(it)
            } else {
                binding.tvEmpty.visibility = (View.INVISIBLE)
                listAdapter.submitList(it)
            }
        })

        filterArea.observe(this@ArenaActivity, Observer {
            when (it) {
                null -> {
                    binding.tvArena.visibility = (View.VISIBLE)
                }

                else -> {
                    binding.tvArena.visibility = (View.INVISIBLE)
                    searchArena("풋살")
                }
            }
        })
    }

    //최상단으로 스크롤
    fun scrolltop() {

    }
}