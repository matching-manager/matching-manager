package com.example.matching_manager.ui.team

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.matching_manager.databinding.TeamDetailActivityBinding
import com.example.matching_manager.ui.team.TeamFragment.Companion.OBJECT_DATA

class TeamDetailActivity : AppCompatActivity() {
    private lateinit var binding: TeamDetailActivityBinding

    companion object {
        fun newIntent(
            item: TeamItem,
            context: Context,
        ): Intent {
            val intent = Intent(context, TeamDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

//    private val data: TeamItem? by lazy {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra(TeamFragment.OBJECT_DATA, TeamItem::class.java)
//        } else {
//            intent.getParcelableExtra<TeamItem>(TeamFragment.OBJECT_DATA)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
//        ivTeam.setImageResource(data.teamProfile)

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }
    }
}