package com.example.matching_manager.ui.match

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchDetailActivityBinding
import com.example.matching_manager.ui.match.MatchFragment.Companion.OBJECT_DATA

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding : MatchDetailActivityBinding

    private val data: MatchData? by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MatchData::class.java)
        }
        else {
            intent.getParcelableExtra<MatchData>(OBJECT_DATA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        ivTeam.setImageResource(data!!.teamImage)

    }
}