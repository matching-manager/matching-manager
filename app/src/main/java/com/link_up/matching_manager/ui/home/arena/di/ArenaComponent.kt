package com.link_up.matching_manager.ui.home.arena.di

import com.link_up.matching_manager.di.DaggerViewModelFactoryModule
import com.link_up.matching_manager.ui.home.arena.ArenaActivity
import com.link_up.matching_manager.ui.home.arena.di.module.ArenaBindModule
import com.link_up.matching_manager.ui.home.arena.di.module.ArenaProvideModule
import com.link_up.matching_manager.ui.home.arena.di.module.ArenaViewModelModule
import dagger.Component

@Component(
    modules = [
        DaggerViewModelFactoryModule::class,
        ArenaProvideModule::class,
        ArenaViewModelModule::class,
        ArenaBindModule::class,
    ]
)
interface ArenaComponent {

    // 의존성을 액티비티에 주입
    fun injectArena(arenaActivity: ArenaActivity)

    @Component.Factory
    interface ArenaFactory {
        fun create(): ArenaComponent
    }
}