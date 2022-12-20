package com.as3arelyoum.di

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.data.repository.AssarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindAssarRepository(
        assarRepositoryImpl: AssarRepositoryImpl
    ): AssarRepository
}