package com.link_up.matching_manager.ui.home.arena

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.link_up.matching_manager.databinding.ArenaActivityBinding
import com.link_up.matching_manager.ui.home.arena.bottomsheet.ArenaFilterCategoryBottomSheet
import com.link_up.matching_manager.ui.home.arena.detail_dialog.ArenaDetailFragment

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

    //스크롤 될 때
    private var onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val fabUpArrow = binding.fabTop
                if (dy > 0) {
                    fabUpArrow.show() // 아래로 스크롤하면 플로팅 버튼 보이기
                } else {
                    fabUpArrow.hide() // 위로 스크롤하면 플로팅 버튼 숨기기
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
        rvArena.addOnScrollListener(onScrollListener)

        fabTop.setOnClickListener {
            smoothScrollTop()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
        btnFutsal.setOnClickListener {
            searchArena("풋살")
            binding.btnFutsal.isChecked = true
            binding.btnSoccer.isChecked = false
            binding.btnBowling.isChecked = false
            binding.btnBasketball.isChecked = false
            binding.btnBadminton.isChecked = false
        }
        btnSoccer.setOnClickListener {
            searchArena("축구장")
            binding.btnFutsal.isChecked = false
            binding.btnSoccer.isChecked = true
            binding.btnBowling.isChecked = false
            binding.btnBasketball.isChecked = false
            binding.btnBadminton.isChecked = false
        }
        btnBowling.setOnClickListener {
            searchArena("볼링장")
            binding.btnFutsal.isChecked = false
            binding.btnSoccer.isChecked = false
            binding.btnBowling.isChecked = true
            binding.btnBasketball.isChecked = false
            binding.btnBadminton.isChecked = false
        }
        btnBasketball.setOnClickListener {
            searchArena("농구장")
            binding.btnFutsal.isChecked = false
            binding.btnSoccer.isChecked = false
            binding.btnBowling.isChecked = false
            binding.btnBasketball.isChecked = true
            binding.btnBadminton.isChecked = false
        }
        btnBadminton.setOnClickListener {
            searchArena("배드민턴장")
            binding.btnFutsal.isChecked = false
            binding.btnSoccer.isChecked = false
            binding.btnBowling.isChecked = false
            binding.btnBasketball.isChecked = false
            binding.btnBadminton.isChecked = true
        }
        btnFilter.setOnClickListener {
            val arenaFilterCategory = ArenaFilterCategoryBottomSheet()

            arenaFilterCategory.setOnFilterSelectedListener(object :
                ArenaFilterCategoryBottomSheet.OnFilterSelectedListener {
                override fun onFilterSelected(selectedArea: String?) {
                    // 선택한 지역값을 받아왔으므로, 이를 ViewModel에 전달합니다.
                    selectedArea?.let { viewModel.setFilterArea(selectedArea) }
                    scrollTop()
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
                listAdapter.submitList(it){
                    //{}아이템을 다 그리고 난후 콜백! 실행
                    //리스트가 모든걸 생성후 최상단으로 스크롤
                    scrollTop()
                }
            }
        })

        filterArea.observe(this@ArenaActivity, Observer {
            when (it) {
                null -> {
                    binding.tvFilter.text = "설정 필요"
                    binding.tvArena.visibility = (View.VISIBLE)
                }

                else -> {
                    binding.tvArena.visibility = (View.INVISIBLE)
                    searchArena("풋살")
                    binding.btnFutsal.isChecked = true
                    binding.btnSoccer.isChecked = false
                    binding.btnBowling.isChecked = false
                    binding.btnBasketball.isChecked = false
                    binding.btnBadminton.isChecked = false
                }
            }
        })
        filter.observe(this@ArenaActivity, Observer { area ->
            val filter = kotlin.String.format("%s", area)

            if (area.contains("선택")) {
                binding.tvFilter.text = "모든 지역"
                binding.tvFilter.visibility = (View.VISIBLE)
            } else {
                binding.tvFilter.text = filter
                binding.tvFilter.visibility = (View.VISIBLE)
            }
        })
    }

    //최상단으로 스크롤
    private fun scrollTop() = with(binding) {
        //약간 딜레이를 주는것
        rvArena.post {
            rvArena.scrollToPosition(0)
        }// 최상단으로 스크롤
    }
    private fun smoothScrollTop() = with(binding) {
        //약간 딜레이를 주는것
        rvArena.post {
            rvArena.smoothScrollToPosition(0)
        }// 최상단으로 스크롤
    }

}