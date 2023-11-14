package com.link_up.matching_manager.ui.my.match

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.databinding.MyMatchActivityBinding
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.my.my.MyFragment
import com.link_up.matching_manager.ui.my.my.MyPostViewModel
import com.link_up.matching_manager.ui.my.my.MyPostViewModelFactory

class MyMatchActivity : AppCompatActivity() {

    private lateinit var binding : MyMatchActivityBinding

    private val viewModel: MyPostViewModel by viewModels {
        MyPostViewModelFactory()
    }

    private val adapter by lazy {
        MyMatchListAdapter(
            onItemClick = {
                startActivity(detailIntent(this, it))
            },
            onMenuClick = {
                val myMatchMenuBottomSheet = MyMatchMenuBottomSheet(it)

                myMatchMenuBottomSheet.show(supportFragmentManager, myMatchMenuBottomSheet.tag)
            }
        )
    }

    companion object {
        fun detailIntent(
            context: Context, item: MatchDataModel
        ): Intent {
            val intent = Intent(context, MyMatchDetailActivity::class.java)
            intent.putExtra(MyFragment.OBJECT_DATA, item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyMatchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        progressBar.visibility = View.VISIBLE
        viewModel.autoFetchMatchData()

        rv.adapter = adapter
        val manager = LinearLayoutManager(this@MyMatchActivity)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() = with(viewModel) {
        matchList.observe(this@MyMatchActivity, Observer {
            var smoothList = 0
            adapter.submitList(it.toList())
            if (it.size > 0) smoothList = it.size - 1
            else smoothList = 1
            binding.progressBar.visibility = View.INVISIBLE
            binding.rv.smoothScrollToPosition(smoothList)
        })
    }
}