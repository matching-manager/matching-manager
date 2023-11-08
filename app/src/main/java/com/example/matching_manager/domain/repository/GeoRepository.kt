package com.example.matching_manager.domain.repository

import android.content.Context
import android.location.Location

interface GeoRepository {

    fun geoCoding(address: String, context : Context): Location
}