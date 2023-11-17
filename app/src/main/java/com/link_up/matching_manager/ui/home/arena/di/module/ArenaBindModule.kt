package com.link_up.matching_manager.ui.home.arena.di.module

import com.link_up.matching_manager.data.repository.GeoRepositoryImpl
import com.link_up.matching_manager.data.repository.api.ArenaRepositoryImpl
import com.link_up.matching_manager.domain.repository.GeoRepository
import com.link_up.matching_manager.domain.repository.api.ArenaRepository
import dagger.Binds
import dagger.Module

@Module
interface ArenaBindModule {

    @Binds
    fun bindArenaRepository(
        repository: ArenaRepositoryImpl
    ) : ArenaRepository

    @Binds
    fun bindsGeoRepository(
        repository: GeoRepositoryImpl
    ) : GeoRepository

    // *** provide 장점 or bind 장점 ***
    //provide -> 제공해줄 것을 만들어주는 것
    //bind -> 바인드를 시켜줄 요소를 만들어 줌
}