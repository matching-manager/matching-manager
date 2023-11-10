package com.link_up.matching_manager.ui.home.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.AnnouncementActivityBinding
import com.link_up.matching_manager.ui.home.home.AnnouncementDetailActivity.Companion.OBJECT_DATA

class AnnouncementActivity : AppCompatActivity() {
    private lateinit var binding : AnnouncementActivityBinding

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }


    private val announcementListAdapter: AnnouncementListAdapter by lazy {
        AnnouncementListAdapter(
            onClick = {
                val intent = Intent(this, AnnouncementDetailActivity::class.java)
                intent.putExtra(OBJECT_DATA, it)
                startActivity(intent)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AnnouncementActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView()  = with(binding) {
        rvAnnouncement.adapter = announcementListAdapter
        rvAnnouncement.addItemDecoration(DividerItemDecoration(this@AnnouncementActivity, LinearLayout.VERTICAL))

        val announcementListManager = LinearLayoutManager(this@AnnouncementActivity)
        announcementListManager.reverseLayout = true
        announcementListManager.stackFromEnd = true
        rvAnnouncement.layoutManager = announcementListManager

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() = with(viewModel) {
        fetchMatchData()
        fetchAnnounceData()
        // viewModel 리스트 변경시 화면에 출력
        announcementList.observe(this@AnnouncementActivity, Observer {
            announcementListAdapter.submitList(it)
        })
    }

}