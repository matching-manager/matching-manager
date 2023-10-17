package com.example.matching_manager.ui.team

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import coil.load
import com.example.matching_manager.databinding.TeamDetailActivityBinding

@Suppress("IMPLICIT_CAST_TO_ANY")
class TeamDetailActivity : AppCompatActivity() {
    private lateinit var binding: TeamDetailActivityBinding

    companion object {
        private const val OBJECT_DATA = "item_object"
        fun newIntent (
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

//        val item: TeamItem? by lazy {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.getParcelableExtra(OBJECT_DATA, TeamItem::class.java)
//            } else {
//                intent.getParcelableExtra<TeamItem>(OBJECT_DATA)
//            }
//        }

        if(1 ==1){
            val item: TeamItem.RecruitmentItem? by lazy {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(OBJECT_DATA, TeamItem.RecruitmentItem::class.java)
                } else {
                    intent.getParcelableExtra<TeamItem.RecruitmentItem>(OBJECT_DATA)
                }
            }
            ivProfile.load(item?.teamProfile)

        }
        else if(2==2){
            val item: TeamItem.ApplicationItem? by lazy {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(OBJECT_DATA, TeamItem.ApplicationItem::class.java)
                } else {
                    intent.getParcelableExtra<TeamItem.ApplicationItem>(OBJECT_DATA)
                }
            }


        }


        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }
    }
}