package com.link_up.matching_manager.util

import android.content.Context
import android.widget.ArrayAdapter
import com.link_up.matching_manager.R

object Spinners {
    fun gameAdapter(context: Context) = ArrayAdapter.createFromResource(
        context,
        R.array.game_array,
        android.R.layout.simple_spinner_item
    )

    fun cityAdapter(context: Context) = ArrayAdapter.createFromResource(
        context,
        R.array.spinner_region,
        android.R.layout.simple_spinner_item
    )


    fun positionToCityResource(position: Int): Int? {
        return when (position) {
            // 시/도 별로 동작을 구현합니다.
            0 -> null
            1 -> R.array.spinner_region_seoul
            2 -> R.array.spinner_region_busan
            3 -> R.array.spinner_region_daegu
            4 -> R.array.spinner_region_incheon
            5 -> R.array.spinner_region_gwangju
            6 -> R.array.spinner_region_daejeon
            7 -> R.array.spinner_region_ulsan
            8 -> R.array.spinner_region_sejong
            9 -> R.array.spinner_region_gyeonggi
            10 -> R.array.spinner_region_gangwon
            11 -> R.array.spinner_region_chung_buk
            12 -> R.array.spinner_region_chung_nam
            13 -> R.array.spinner_region_jeon_buk
            14 -> R.array.spinner_region_jeon_nam
            15 -> R.array.spinner_region_gyeong_buk
            16 -> R.array.spinner_region_gyeong_nam
            17 -> R.array.spinner_region_jeju
            else -> null
        }
    }

    fun positionToDongResource(position: Int): Int? {
        return when (position) {
            0 -> R.array.spinner_region_seoul_gangnam
            1 -> R.array.spinner_region_seoul_gangdong
            2 -> R.array.spinner_region_seoul_gangbuk
            3 -> R.array.spinner_region_seoul_gangseo
            4 -> R.array.spinner_region_seoul_gwanak
            5 -> R.array.spinner_region_seoul_gwangjin
            6 -> R.array.spinner_region_seoul_guro
            7 -> R.array.spinner_region_seoul_geumcheon
            8 -> R.array.spinner_region_seoul_nowon
            9 -> R.array.spinner_region_seoul_dobong
            10 -> R.array.spinner_region_seoul_dongdaemun
            11 -> R.array.spinner_region_seoul_dongjag
            12 -> R.array.spinner_region_seoul_mapo
            13 -> R.array.spinner_region_seoul_seodaemun
            14 -> R.array.spinner_region_seoul_seocho
            15 -> R.array.spinner_region_seoul_seongdong
            16 -> R.array.spinner_region_seoul_seongbuk
            17 -> R.array.spinner_region_seoul_songpa
            18 -> R.array.spinner_region_seoul_yangcheon
            19 -> R.array.spinner_region_seoul_yeongdeungpo
            20 -> R.array.spinner_region_seoul_yongsan
            21 -> R.array.spinner_region_seoul_eunpyeong
            22 -> R.array.spinner_region_seoul_jongno
            23 -> R.array.spinner_region_seoul_jung
            24 -> R.array.spinner_region_seoul_jungnanggu
            else -> null
        }
    }

    fun genderAdapter(context: Context) = ArrayAdapter.createFromResource(
        context,
        R.array.gender_array,
        android.R.layout.simple_spinner_item
    )

    fun levelAdapter(context: Context) = ArrayAdapter.createFromResource(
        context,
        R.array.level_array,
        android.R.layout.simple_spinner_item
    )

    fun timeAdapter(context: Context) = ArrayAdapter.createFromResource(
        context,
        R.array.time_array,
        android.R.layout.simple_spinner_item
    )

}