package com.link_up.matching_manager.domain.usecase.geo

import android.content.Context
import android.location.Location
import com.link_up.matching_manager.domain.repository.GeoRepository
import javax.inject.Inject

class GetGeoLocationUseCase @Inject constructor(
    private val repository: GeoRepository
) {
    operator fun invoke(
        address: String,
        context: Context
    ): Location = repository.geoCoding(
        address = address,
        context = context
    )
}