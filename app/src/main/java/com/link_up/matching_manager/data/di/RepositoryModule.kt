package com.link_up.matching_manager.data.di

import com.link_up.matching_manager.data.remote.ArenaRemoteDataSource
import com.link_up.matching_manager.data.repository.api.ArenaRepositoryImpl
import com.link_up.matching_manager.domain.repository.ArenaRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesArenaRepository(dataSource: ArenaRemoteDataSource) : ArenaRepository{
        return ArenaRepositoryImpl(dataSource)
    }

    // *** provide 장점 or bind 장점 ***
    //provide -> 제공해줄 것을 만들어주는 것
    //bind -> 바인드를 시켜줄 요소를 만들어 줌
}