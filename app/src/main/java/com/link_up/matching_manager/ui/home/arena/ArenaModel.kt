package com.link_up.matching_manager.ui.home.arena

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArenaModel(
    val placeName: String?,
    val phone: String?,
    val addressName: String?, // 지번 주소
    val roadAddressName: String?, // 도로명 주소
    val placeUrl: String?, // 장소 상세페이지 URL
    // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) (단위 meter)
    val distance: String?,
) : Parcelable