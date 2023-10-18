package com.example.matching_manager.ui.match

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchDetailActivityBinding
import com.example.matching_manager.ui.match.MatchFragment.Companion.OBJECT_DATA
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding: MatchDetailActivityBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val bottomSheetLayout by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet_layout) }
    private val bottomSheetBookmarkButton by lazy { findViewById<ImageView>(R.id.iv_bookmark) }
    private val bottomSheetCallMatchButton by lazy { findViewById<Button>(R.id.btn_call_match) }

    private val data: MatchDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MatchDataModel::class.java)
        } else {
            intent.getParcelableExtra<MatchDataModel>(OBJECT_DATA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        ivProfile.setImageResource(data!!.userImg)

        initializePersistentBottomSheet()
        persistentBottomSheetEvent()

        scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > 0) {
                // 아래로 스크롤 중
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                // 위로 스크롤 중
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            if(oldScrollY > scrollY) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initializePersistentBottomSheet() {

        // BottomSheetBehavior에 layout 설정
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("MatchDetailActivity", "state: hidden")
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d("MatchDetailActivity", "state: expanded")
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("MatchDetailActivity", "state: collapsed")
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Log.d("MatchDetailActivity", "state: dragging")
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d("MatchDetailActivity", "state: settling")
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.d("MatchDetailActivity", "state: half expanded")
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

    }

    // PersistentBottomSheet 내부 버튼 click event
    private fun persistentBottomSheetEvent() {
    }
}