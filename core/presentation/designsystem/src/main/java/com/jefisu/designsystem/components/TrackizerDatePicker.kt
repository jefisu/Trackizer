@file:OptIn(ExperimentalLayoutApi::class, ExperimentalContracts::class)

package com.jefisu.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.dropShadow
import com.jefisu.designsystem.util.rippleClickable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.contracts.ExperimentalContracts

@Composable
fun TrackizerDatePicker(
    state: DatePickerState = rememberTrackizerDatePickerState(),
    shape: Shape = Shapes().medium,
    colors: DatePickerColors = TrackizerDatePickerDefaults.colors(),
    modifier: Modifier = Modifier,
) {
    val years by rememberUpdatedState(generateCalendarYears(state.date))

    Column(modifier = modifier) {
        DatePickerHeader(
            state = state,
            years = years,
            shape = shape,
            colors = colors,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.medium))
        DatePickerBody(
            state = state,
            years = years,
            colors = colors,
        )
    }
}

@Composable
private fun DatePickerHeader(
    state: DatePickerState,
    years: List<Int>,
    shape: Shape,
    colors: DatePickerColors,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Min),
    ) {
        NavigationRow(
            state = state,
            years = years,
            shape = shape,
            colors = colors,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(TrackizerTheme.spacing.medium))
        YearMonthToggle(
            state = state,
            shape = shape,
            colors = colors,
        )
    }
}

@Composable
private fun NavigationRow(
    state: DatePickerState,
    years: List<Int>,
    shape: Shape,
    colors: DatePickerColors,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
        modifier = modifier
            .outerDropShadow(shape, colors.dropShadowColor)
            .background(colors.backgroundColor, shape),
    ) {
        IconButton(onClick = { state.selectYearOrMonth(isAdding = false) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                tint = colors.iconColor,
            )
        }
        Spacer(Modifier.weight(1f))
        AnimatedMonth(
            date = state.date,
            previousDate = state.previousDate,
            years = years,
            isSelectingDay = state.isSelectingDay,
            colors = colors,
        )
        Canvas(modifier = Modifier.size(6.dp)) {
            drawCircle(color = colors.selectedDateContainerColor)
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { state.selectYearOrMonth(isAdding = true) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = colors.iconColor,
            )
        }
    }
}

@Composable
private fun YearMonthToggle(
    state: DatePickerState,
    shape: Shape,
    colors: DatePickerColors,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight()
            .width(90.dp)
            .outerDropShadow(shape, colors.dropShadowColor)
            .clip(shape)
            .background(colors.backgroundColor)
            .rippleClickable { state.toggleSelectionMode() },
    ) {
        Text(
            text = with(state) {
                if (isSelectingDay) {
                    date.year.toString()
                } else {
                    date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                }
            },
            style = TrackizerTheme.typography.headline3,
            color = colors.dateColor,
        )
    }
}

@Composable
private fun DatePickerBody(
    state: DatePickerState,
    years: List<Int>,
    colors: DatePickerColors,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.extraMedium),
        modifier = Modifier
            .outerDropShadow(Shapes().medium, colors.dropShadowColor)
            .animateContentSize()
            .background(colors.backgroundColor, Shapes().medium)
            .padding(TrackizerTheme.spacing.medium),
    ) {
        with(state) {
            if (isSelectingDay) {
                WeekDays(colors = colors)
                CalendarDays(
                    state = state,
                    colors = colors,
                )
            } else {
                CalendarYears(
                    years = years,
                    state = state,
                    colors = colors,
                )
            }
        }
    }
}

@Composable
private fun DatePickerCalendar(content: @Composable FlowRowScope.() -> Unit) {
    FlowRow(
        maxItemsInEachRow = MAX_COLUMNS,
        verticalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.extraMedium),
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.extraSmall),
    ) {
        content()
    }
}

@Composable
private fun AnimatedMonth(
    date: LocalDate,
    previousDate: LocalDate,
    years: List<Int>,
    isSelectingDay: Boolean,
    colors: DatePickerColors,
) {
    val text = remember(date, isSelectingDay) {
        if (isSelectingDay) {
            date.month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault(),
            )
        } else {
            "${years.first()} - ${years.last()}"
        }
    }

    DateSlideAnimation(
        targetState = text,
        currentDate = date,
        previousDate = previousDate,
    ) {
        Text(
            text = it,
            style = TrackizerTheme.typography.headline3,
            color = colors.dateColor,
        )
    }
}

@Composable
fun WeekDays(colors: DatePickerColors) {
    val daysOfWeek = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
    )
    DatePickerCalendar {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = TrackizerTheme.typography.headline2,
                color = colors.dateColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CalendarDays(
    state: DatePickerState,
    colors: DatePickerColors,
) {
    val selectedDate = state.date
    val previousDate = state.previousDate
    val days by rememberUpdatedState(generateCalendarDays(selectedDate))

    DateSlideAnimation(
        targetState = days,
        currentDate = selectedDate,
        previousDate = previousDate,
    ) {
        DatePickerCalendar {
            it.forEach { day ->
                val isSelected = rememberSaveable(selectedDate) { selectedDate == day.date }
                CalendarText(
                    text = String.format(Locale.getDefault(), "%02d", day.date.dayOfMonth),
                    onClick = { state.updateSelectedDate(day.date) },
                    isSelected = isSelected,
                    color = when {
                        day.isCurrentMonth -> colors.dateColor
                        day.date.isEqual(selectedDate) -> colors.dateColor
                        else -> colors.dateDisabledColor
                    },
                    selectedColor = colors.selectedDateContainerColor,
                )
            }
        }
    }
}

@Composable
private fun CalendarYears(
    state: DatePickerState,
    years: List<Int>,
    colors: DatePickerColors,
) {
    DateSlideAnimation(
        targetState = years,
        currentDate = state.date,
        previousDate = state.previousDate,
    ) {
        DatePickerCalendar {
            it.forEach { year ->
                val isSelected = rememberSaveable(state.date) { state.date.year == year }
                CalendarText(
                    text = year.toString(),
                    onClick = { state.updateSelectedDate(state.date.withYear(year)) },
                    isSelected = isSelected,
                    color = if (isSelected) colors.dateColor else colors.dateDisabledColor,
                    selectedColor = colors.selectedDateContainerColor,
                    indicationRadius = 20.dp,
                )
            }
        }
    }
}

@Composable
private fun FlowRowScope.CalendarText(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    color: Color,
    selectedColor: Color,
    indicationRadius: Dp = 16.dp,
    modifier: Modifier = Modifier,
) {
    val scaleAnim by animateFloatAsState(targetValue = if (isSelected) 1f else 0f)

    Text(
        text = text,
        style = TrackizerTheme.typography.headline2,
        textAlign = TextAlign.Center,
        color = color,
        modifier = modifier
            .weight(1f)
            .rippleClickable(
                indication = rememberRipple(radius = indicationRadius, bounded = false),
                onClick = onClick,
            )
            .drawBehind {
                scale(scaleAnim) {
                    drawCircle(color = selectedColor, radius = indicationRadius.toPx())
                }
            },
    )
}

@Composable
private fun <T> DateSlideAnimation(
    targetState: T,
    currentDate: LocalDate,
    previousDate: LocalDate,
    content: @Composable (T) -> Unit,
) {
    val direction = if (currentDate.isAfter(previousDate)) -1 else 1
    val durationMillis = 300

    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { direction * it },
                animationSpec = tween(durationMillis),
            ) + fadeIn(animationSpec = tween(durationMillis)) togetherWith
                    slideOutHorizontally(
                        targetOffsetX = { -direction * it },
                        animationSpec = tween(durationMillis),
                    ) + fadeOut(animationSpec = tween(durationMillis))
        },
        content = { content(it) },
    )
}

@Preview
@Composable
private fun TrackizerDatePickerPreview() {
    TrackizerTheme {
        TrackizerDatePicker(
            modifier = Modifier.padding(TrackizerTheme.spacing.medium),
        )
    }
}

@Composable
fun rememberTrackizerDatePickerState(
    initialDate: LocalDate = LocalDate.now(),
    isSelectingDay: Boolean = true,
): DatePickerState =
    rememberSaveable(saver = DatePickerState.saver()) {
        DatePickerState(
            initialDate = initialDate,
            isSelectingDay = isSelectingDay,
        )
    }

private fun generateCalendarDays(selectedDate: LocalDate): List<Day> {
    val yearMonth = YearMonth.from(selectedDate)
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % MAX_COLUMNS

    val days = mutableListOf<Day>()

    val previousMonthDays = firstDayOfWeek
    if (previousMonthDays > 0) {
        val previousMonth = yearMonth.minusMonths(1)
        val previousMonthDaysCount = previousMonth.lengthOfMonth()
        for (i in (previousMonthDaysCount - previousMonthDays + 1)..previousMonthDaysCount) {
            days.add(Day(previousMonth.atDay(i), isCurrentMonth = false))
        }
    }

    for (i in 1..daysInMonth) {
        days.add(Day(firstDayOfMonth.withDayOfMonth(i), isCurrentMonth = true))
    }

    val remainingDays = TOTAL_ITEMS - days.size
    if (remainingDays > 0) {
        val nextMonth = yearMonth.plusMonths(1)
        for (i in 1..remainingDays) {
            days.add(Day(nextMonth.atDay(i), isCurrentMonth = false))
        }
    }

    return days
}

private fun generateCalendarYears(selectedDate: LocalDate): List<Int> {
    val currentYear = selectedDate.year
    val startYear = currentYear - (currentYear % TOTAL_ITEMS)

    return (0 until TOTAL_ITEMS).map { startYear + it }
}

private fun Modifier.outerDropShadow(
    shape: Shape,
    color: Color,
) = this.dropShadow(
    shape = shape,
    color = color.copy(0.15f),
    offsetX = (-4).dp,
    offsetY = (-4).dp,
)

class DatePickerState(initialDate: LocalDate, isSelectingDay: Boolean) {

    var date by mutableStateOf(initialDate)
        private set

    var previousDate by mutableStateOf(initialDate)
        private set

    var isSelectingDay by mutableStateOf(isSelectingDay)
        private set

    internal fun toggleSelectionMode() {
        isSelectingDay = !isSelectingDay
    }

    internal fun updateSelectedDate(newDate: LocalDate) {
        previousDate = date
        date = newDate
    }

    internal fun selectYearOrMonth(isAdding: Boolean) {
        val value = if (isAdding) 1L else -1L
        if (isSelectingDay) {
            date = date.plusMonths(value)
            return
        }
        date = date.plusYears(value * TOTAL_ITEMS)
    }

    companion object {
        internal fun saver(): Saver<DatePickerState, Any> = listSaver(
            save = {
                listOf(
                    it.date.toEpochDay(),
                    it.isSelectingDay,
                )
            },
            restore = {
                DatePickerState(
                    initialDate = LocalDate.ofEpochDay(it[0] as Long),
                    isSelectingDay = it[1] as Boolean,
                )
            },
        )
    }
}

data class Day(val date: LocalDate, val isCurrentMonth: Boolean)

data class DatePickerColors(
    val dateColor: Color,
    val dateDisabledColor: Color,
    val selectedDateContainerColor: Color,
    val backgroundColor: Color,
    val dropShadowColor: Color,
    val iconColor: Color,
)

object TrackizerDatePickerDefaults {

    @Composable
    fun colors(
        dateColor: Color = Color.White,
        dateDisabledColor: Color = Gray30,
        selectedDateContainerColor: Color = AccentPrimary100,
        backgroundColor: Color = Gray70,
        dropShadowColor: Color = Color.White.copy(alpha = 0.15f),
        iconColor: Color = Gray30,
    ) = DatePickerColors(
        dateColor = dateColor,
        dateDisabledColor = dateDisabledColor,
        selectedDateContainerColor = selectedDateContainerColor,
        backgroundColor = backgroundColor,
        dropShadowColor = dropShadowColor,
        iconColor = iconColor,
    )
}

private const val MAX_LINES = 6
private const val MAX_COLUMNS = 7
private const val TOTAL_ITEMS = MAX_COLUMNS * MAX_LINES
