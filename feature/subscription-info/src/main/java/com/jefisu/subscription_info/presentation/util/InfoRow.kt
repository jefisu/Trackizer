package com.jefisu.subscription_info.presentation.util

import androidx.annotation.StringRes
import com.jefisu.subscription_info.R

data class InfoRow(val value: String, val label: String?, val type: InfoRowType)

enum class InfoRowType(@StringRes val titleId: Int) {
    Name(R.string.name),
    Description(R.string.description),
    Category(R.string.category),
    FirstPayment(R.string.first_payment),
    Reminder(R.string.reminder),
    CreditCard(R.string.credit_card),
}
