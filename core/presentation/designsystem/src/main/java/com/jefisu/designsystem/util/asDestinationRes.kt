package com.jefisu.designsystem.util

import com.jefisu.designsystem.R
import com.jefisu.ui.navigation.Destination

fun Destination.asDestinationRes(): Int = when (this) {
    is Destination.HomeScreen -> R.drawable.ic_home
    is Destination.SpendingBudgetsScreen -> R.drawable.ic_budgets
    is Destination.CalendarScreen -> R.drawable.ic_calendar
    is Destination.CreditCardScreen -> R.drawable.ic_credit_cards
    is Destination.AddSubscriptionScreen -> R.drawable.ic_rounded_add
    else -> error("Invalid destination: $this")
}