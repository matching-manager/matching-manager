package com.link_up.matching_manager.ui.home.arena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.GeoRepositoryImpl
import com.link_up.matching_manager.domain.repository.ArenaRepository
import com.link_up.matching_manager.domain.repository.GeoRepository
import com.link_up.matching_manager.domain.usecase.api.GetArenaInfoUseCase
import com.link_up.matching_manager.domain.usecase.geo.GetGeoLocationUseCase
import javax.inject.Inject

// 1
// viewModelFactory inject용 코드
// viewModelFactory를 DI화
// 구글에 검색 @ViewModelKey -> 생성자가 사라짐 inject사용

// 2

class ArenaViewModelFactory @Inject constructor(private val arenaRepository : ArenaRepository) : ViewModelProvider.Factory {

//    private val arenaRepository: ArenaRepository = ArenaRepositoryImpl(
//        RetrofitClient.search
//    )

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