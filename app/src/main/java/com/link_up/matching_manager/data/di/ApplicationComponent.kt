package com.link_up.matching_manager.data.di

import com.link_up.matching_manager.ui.home.arena.ArenaActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface ApplicationComponent {

    // 의존성을 액티비티에 주입
    fun injectArena(arenaActivity: ArenaActivity)
}