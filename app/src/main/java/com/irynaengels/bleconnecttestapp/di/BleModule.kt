package com.irynaengels.bleconnecttestapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.irynaengels.bleconnecttestapp.data.BleServiceImpl
import com.irynaengels.bleconnecttestapp.domain.BleService
import com.polidea.rxandroidble3.RxBleClient
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BleModule {

    @Provides
    fun provideRxBleClient(@ApplicationContext context: Context): RxBleClient {
        return RxBleClient.create(context)
    }

    @Provides
    @Singleton
    fun provideBleService(rxBleClient: RxBleClient): BleService {
        return BleServiceImpl(rxBleClient)
    }
}
