package com.jefisu.ui.ext.draw_scope

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawArcBlur(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    useCenter: Boolean,
    maxBlurArcs: Int = 20,
    cap: StrokeCap = StrokeCap.Round,
) {
    for (i in 0..maxBlurArcs) {
        drawArc(
            color = color.copy(alpha = i / 900f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = useCenter,
            style = Stroke(
                width = 80f + (maxBlurArcs - i) * 10,
                cap = cap,
            ),
        )
    }
}
