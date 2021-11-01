package com.example.myapplication.ApplicationUtils

import com.example.myapplication.repository.ApparelRepositoryInMemory
import com.example.myapplication.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationContainer {

    companion object {
        private var apparelRepository: Repository? = null
    }

    fun getSingletonApparelRepository(): Repository? {
        if (apparelRepository == null) {
            apparelRepository = ApparelRepositoryInMemory()
        }
        return apparelRepository
    }

    @Provides
    @Singleton
    fun provideApparelRepository(repository: ApparelRepositoryInMemory): Repository {
        return ApparelRepositoryInMemory()
    }
}