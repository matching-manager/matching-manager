package com.example.matching_manager.ui.home.arena

data class ArenaModel(
    private val placeName: String?,
    private val phone: String?,
    private val addressName: String?, // 지번 주소
    private val roadAddressName: String?, // 도로명 주소
    private val placeUrl: String?, // 장소 상세페이지 URL
    // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) (단위 meter)
    val distance: String?,
)