package com.jefisu.calendar.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: SubscriptionRepository) :
    ViewModel() {

    var state by mutableStateOf(CalendarState())
        private set

    init {
        repository.subscriptions
            .onEach { state = state.copy(subscriptions = it) }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CalendarAction) {
        when (action) {
            is CalendarAction.SelectDay -> {
                state = state.copy(selectedDay = action.localDate)
            }

            is CalendarAction.SelectMonth -> {
                with(state) {
                    state = copy(
                        selectedMonth = selectedMonth.withMonth(action.month.value),
                        selectedDay = selectedDay.withMonth(action.month.value),
                    )
                }
            }

            is CalendarAction.ToggleMonthPicker -> {
                state = state.copy(
                    showMonthPicker = !state.showMonthPicker,
                )
            }
        }
    }
}
