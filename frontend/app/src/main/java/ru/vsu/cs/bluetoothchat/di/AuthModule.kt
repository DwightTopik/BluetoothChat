package ru.vsu.cs.bluetoothchat.di

import ru.vsu.cs.bluetoothchat.data.auth.AuthApi
import ru.vsu.cs.bluetoothchat.data.auth.AuthRepository
import ru.vsu.cs.bluetoothchat.data.auth.AuthRepositoryImpl
import ru.vsu.cs.bluetoothchat.domain.auth.AuthUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthUseCase(repository: AuthRepository): AuthUseCase {
            return AuthUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
} 