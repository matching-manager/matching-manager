package com.link_up.matching_manager.ui.home.home

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.AnnouncementDetailActivityBinding
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.match.MatchDetailActivity

class AnnouncementDetailActivity : AppCompatActivity() {
    private lateinit var binding : AnnouncementDetailActivityBinding

    companion object {
        const val OBJECT_DATA = "item_object"
    }

    private val data: AnnouncementDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MatchDetailActivity.OBJECT_DATA, AnnouncementDataModel::class.java)
        } else {
            intent.getParcelableExtra<AnnouncementDataModel>(OBJECT_DATA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AnnouncementDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        tvTitle.text = data!!.title
        tvDate.text = data!!.uploadDate
        tvContent.text = data!!.content

        btnCancel.setOnClickListener {
            finish()
        }
    }
}