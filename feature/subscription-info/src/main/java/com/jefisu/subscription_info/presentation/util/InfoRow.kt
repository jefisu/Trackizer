package com.jefisu.subscription_info.presentation.util

import androidx.annotation.StringRes
import com.jefisu.subscription_info.R
import com.jefisu.ui.R as UiRes

data class InfoRow(val value: String, val label: String?, val type: InfoRowType)

enum class InfoRowType(@StringRes val titleId: Int) {
    Name(R.string.name),
    Description(UiRes.string.description),
    Category(UiRes.string.category),
    FirstPayment(R.string.first_payment),
    Reminder(R.string.reminder),
    CreditCard(UiRes.string.credit_card),
}
