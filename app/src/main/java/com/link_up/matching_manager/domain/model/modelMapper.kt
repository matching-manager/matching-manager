package com.link_up.matching_manager.domain.model

import com.link_up.matching_manager.data.model.ArenaResponse
import com.link_up.matching_manager.data.model.DocumentsResponse
import com.link_up.matching_manager.data.model.MetaResponse
import com.link_up.matching_manager.data.model.SameNameResponse

fun com.link_up.matching_manager.data.model.ArenaResponse.toArenaEntity() = ArenaEntity(
    meta = meta?.toEntity(),
    documents = documents?.map { response ->
        response.toEntity()
    }
)

fun com.link_up.matching_manager.data.model.MetaResponse.toEntity() = MetaEntity(
    totalCount = totalCount,
    pageableCount = pageableCount,
    isEnd = isEnd,
    sameName = sameName?.toEntity()
)

fun com.link_up.matching_manager.data.model.SameNameResponse.toEntity() = SameNameEntity(
    region = region,
    keyword = keyword,
    selectedRegion = selectedRegion
)

fun com.link_up.matching_manager.data.model.DocumentsResponse.toEntity() = DocumentsEntity(
    placeName = placeName,
    phone = phone,
    addressName = addressName,
    roadAddressName = roadAddressName,
    longitude = longitude,
    latitude = latitude,
    placeUrl = placeUrl,
    distance = distance,
)
