package com.example.matching_manager.domain.model

import com.example.matching_manager.data.model.ArenaResponse
import com.example.matching_manager.data.model.DocumentsResponse
import com.example.matching_manager.data.model.MetaResponse
import com.example.matching_manager.data.model.SameNameResponse

fun ArenaResponse.toArenaEntity() = ArenaEntity(
    meta = meta?.toEntity(),
    documents = documents?.toEntity()
)

fun MetaResponse.toEntity() = MetaEntity(
    totalCount = totalCount,
    pageableCount = pageableCount,
    isEnd = isEnd,
    sameName = sameName?.toEntity()
)

fun SameNameResponse.toEntity() = SameNameEntity(
    region = region,
    keyword = keyword,
    selectedRegion = selectedRegion
)

fun DocumentsResponse.toEntity() = DocumentsEntity(
    placeName = placeName,
    phone = phone,
    addressName = addressName,
    roadAddressName = roadAddressName,
    longitude = longitude,
    latitude = latitude,
    placeUrl = placeUrl,
    distance = distance,
)
