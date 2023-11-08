package com.link_up.matching_manager.ui.home.arena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.ArenaRepositoryImpl
import com.link_up.matching_manager.data.repository.GeoRepositoryImpl
import com.link_up.matching_manager.domain.repository.ArenaRepository
import com.link_up.matching_manager.domain.repository.GeoRepository
import com.link_up.matching_manager.domain.usecase.GetArenaInfoUseCase
import com.link_up.matching_manager.domain.usecase.geo.GetGeoLocationUseCase
import com.link_up.matching_manager.retrofit.RetrofitClient

class ArenaViewModelFactory : ViewModelProvider.Factory {

    private val arenaRepository: ArenaRepository = ArenaRepositoryImpl(
        RetrofitClient.search
    )
    private val geoRepository : GeoRepository = GeoRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArenaViewModel::class.java)) {
            return ArenaViewModel(
                GetArenaInfoUseCase(arenaRepository),
                GetGeoLocationUseCase(geoRepository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}