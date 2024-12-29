package com.jefisu.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
internal class CalendarViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state = _state.combine(repository.allData) { _, subscriptions ->
        _state.updateAndGet {
            it.copy(subscriptions = subscriptions)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        _state.value,
    )

    fun onAction(action: CalendarAction) {
        when (action) {
            is CalendarAction.SelectDay -> {
                _state.update { it.copy(selectedDay = action.localDate) }
            }

            is CalendarAction.SelectMonth -> {
                _state.update {
                    it.copy(
                        selectedMonth = it.selectedMonth.withMonth(action.month.value),
                        selectedDay = it.selectedDay.withMonth(action.month.value),
                    )
                }
            }

            is CalendarAction.Navigate -> {
                viewModelScope.launch { navigator.navigate(action.destination) }
            }
        }
    }
}