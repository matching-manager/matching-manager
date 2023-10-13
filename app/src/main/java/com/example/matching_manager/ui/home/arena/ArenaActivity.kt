package com.example.matching_manager.ui.home.arena

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.ArenaActivityBinding

class ArenaActivity : AppCompatActivity() {

    private val binding by lazy { ArenaActivityBinding.inflate(layoutInflater) }

    private val viewModel : ArenaViewModel by viewModels()

    private val listAdapter : ArenaListAdapter by lazy {
        ArenaListAdapter(
            onClick = { item ->
                // Click Evnet
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initModel()
    }

    private fun initView() = with(binding){
        rvArena.adapter = listAdapter
    }

    private fun initModel() = with(viewModel){
        list.observe(this@ArenaActivity, Observer {
            listAdapter.submitList(it)
        })
    }
}