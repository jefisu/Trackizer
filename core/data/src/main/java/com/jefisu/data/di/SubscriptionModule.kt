package com.jefisu.data.di

import com.jefisu.data.repository.SubscriptionRepositoryImpl
import com.jefisu.domain.repository.SubscriptionRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val subscriptionModule = module {
    singleOf(::SubscriptionRepositoryImpl).bind<SubscriptionRepository>()
}

object SubscriptionModuleScope {
    const val VIEW_MODEL = "viewModelScope"
}
