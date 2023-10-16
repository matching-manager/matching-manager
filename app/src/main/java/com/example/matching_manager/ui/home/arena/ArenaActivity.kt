package com.example.matching_manager.ui.home.arena

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.ArenaActivityBinding
import com.example.matching_manager.ui.home.arena.detail_dialog.ArenaDetailFragment

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

    companion object{
        fun newIntent(
            context: Context
        ) = Intent(context,ArenaActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {
        rvArena.adapter = listAdapter
        searchArena("풋살")

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
    }

    private fun searchArena(text: String) {
        viewModel.searchArena(text, this)
    }

    private fun initModel() = with(viewModel) {
        list.observe(this@ArenaActivity, Observer {
            listAdapter.submitList(it)
        })
    }
}