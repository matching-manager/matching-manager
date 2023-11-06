package com.example.matching_manager.ui.my

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matching_manager.databinding.MyMatchActivityBinding
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.signin.UserInformation

class MyMatchActivity : AppCompatActivity() {

    private lateinit var binding : MyMatchActivityBinding

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
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
        viewModel.fetchMatchData(UserInformation.userInfo.uid!!)

        rv.adapter = adapter
        val manager = LinearLayoutManager(this@MyMatchActivity)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        rv.layoutManager = manager

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() = with(viewModel) {
        autoFetchMatchData()

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