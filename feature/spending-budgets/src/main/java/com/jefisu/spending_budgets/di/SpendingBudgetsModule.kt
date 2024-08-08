package com.jefisu.spending_budgets.di

import com.jefisu.spending_budgets.presentation.SpendingBudgetsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val spendingBudgetsModule = module {
    viewModelOf(::SpendingBudgetsViewModel)
}
