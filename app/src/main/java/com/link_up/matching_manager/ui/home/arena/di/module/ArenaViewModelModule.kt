package com.link_up.matching_manager.ui.home.arena.di.module

import androidx.lifecycle.ViewModel
import com.link_up.matching_manager.di.ViewModelKey
import com.link_up.matching_manager.ui.home.arena.ArenaViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ArenaViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArenaViewModel::class)
    fun bindsArenaViewModel(viewModel: ArenaViewModel) : ViewModel
}