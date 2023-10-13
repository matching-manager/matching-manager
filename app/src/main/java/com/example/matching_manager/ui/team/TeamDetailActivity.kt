package com.example.matching_manager.ui.team

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.matching_manager.databinding.MainActivityBinding
import com.example.matching_manager.databinding.MatchDetailActivityBinding
import com.example.matching_manager.databinding.TeamDetailActivityBinding
import com.example.matching_manager.ui.match.MatchData
import com.example.matching_manager.ui.match.MatchFragment

class TeamDetailActivity : AppCompatActivity() {
    private lateinit var binding: TeamDetailActivityBinding

    private val data: TeamModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TeamFragment.OBJECT_DATA, TeamModel::class.java)
        } else {
            intent.getParcelableExtra<TeamModel>(TeamFragment.OBJECT_DATA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        ivTeam.setImageResource(data!!.teamProfile)

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }
    }
}