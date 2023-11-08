package com.link_up.matching_manager.domain.model

data class ArenaEntity(
    val meta: MetaEntity?,
    val documents: List<DocumentsEntity>?
)

data class MetaEntity(
    // 검색어에 검색된 문서 수
    val totalCount: Int?,
    // total_count 중 노출 가능 문서 수 (최대: 45)
    val pageableCount: Int?,
    // 현재 페이지가 마지막 페이지인지 여부 -> 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
    val isEnd: Boolean?,
    // 질의어의 지역 및 키워드 분석 정보
    val sameName: SameNameEntity?
)

data class SameNameEntity(
    // 질의어에서 인식된 지역의 리스트 ( 예: '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트 )
    val region: Array<String>?,
    // 질의어에서 지역 정보를 제외한 키워드 ( 예: '중앙로 맛집' 에서 '맛집' )
    val keyword: String?,
    // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
    val selectedRegion: String?,
)

data class DocumentsEntity(
    // 장소명, 업체명
    val placeName: String?,
    // 전화 번호
    val phone: String?,
    // 전체 지번 주소
    val addressName: String?,
    // 전체 도로명 주소
    val roadAddressName: String?,
    // X 좌표값, 경위도인 경우 longitude (경도)
    val longitude: String?,
    // Y 좌표값, 경위도인 경우 latitude(위도)
    val latitude: String?,
    // 장소 상세페이지 URL
    val placeUrl: String?,
    // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) (단위 meter)
    val distance: String?,
)