package com.jefisu.data.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(Dispatchers::IO)
    includes(userModule, subscriptionModule)
}
