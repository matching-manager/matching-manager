package com.example.matching_manager.domain.usecase.geo

import android.content.Context
import android.location.Location
import com.example.matching_manager.domain.repository.GeoRepository

class GetGeoLocationUseCase(
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