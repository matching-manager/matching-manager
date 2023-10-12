package com.example.matching_manager.data.model

import com.google.gson.annotations.SerializedName

data class ArenaResponse(
    @SerializedName("meta") val meta: MetaResponse?,
    @SerializedName("documents") val documents: DocumentsResponse?
)

data class MetaResponse(
    // 검색어에 검색된 문서 수
    @SerializedName("total_count") val totalCount: Int?,
    // total_count 중 노출 가능 문서 수 (최대: 45)
    @SerializedName("pageable_count") val pageableCount: Int?,
    // 현재 페이지가 마지막 페이지인지 여부 -> 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
    @SerializedName("is_end") val isEnd: Boolean?,
    // 질의어의 지역 및 키워드 분석 정보
    @SerializedName("same_name") val sameName: SameNameResponse?
)

data class SameNameResponse(
    // 질의어에서 인식된 지역의 리스트 ( 예: '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트 )
    @SerializedName("region") val region: String?,
    // 질의어에서 지역 정보를 제외한 키워드 ( 예: '중앙로 맛집' 에서 '맛집' )
    @SerializedName("keyword") val keyword: String?,
    // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
    @SerializedName("selected_region") val selectedRegion: String?,
)

data class DocumentsResponse(
    // 장소명, 업체명
    @SerializedName("place_name") val placeName: String?,
    // 전화 번호
    @SerializedName("phone") val phone: String?,
    // 전체 지번 주소
    @SerializedName("address_name") val addressName: String?,
    // 전체 도로명 주소
    @SerializedName("road_address_name") val roadAddressName: String?,
    // X 좌표값, 경위도인 경우 longitude (경도)
    @SerializedName("x") val longitude: String?,
    // Y 좌표값, 경위도인 경우 latitude(위도)
    @SerializedName("y") val latitude: String?,
    // 장소 상세페이지 URL
    @SerializedName("place_url") val placeUrl: String?,
    // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) (단위 meter)
    @SerializedName("distance") val distance: String?,
)