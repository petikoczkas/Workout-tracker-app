package hu.bme.aut.workout_tracker.ui.view.chart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import java.math.RoundingMode

@SuppressLint("RememberReturnType")
@Composable
fun WorkoutTrackerChart(
    chartEntryModel: ChartEntryModel,
) {
    val marker = rememberMarker()
    val difference = chartEntryModel.maxY - chartEntryModel.minY
    Chart(
        chart = lineChart(
            axisValuesOverrider = AxisValuesOverrider.fixed(
                minY = if (difference != 0f) chartEntryModel.minY - difference / 8 else chartEntryModel.minY - 10,
                maxY = if (difference != 0f) chartEntryModel.maxY + difference / 8 else chartEntryModel.maxY + 10,
            ),
        ),
        model = chartEntryModel,
        startAxis = rememberStartAxis(
            title = stringResource(R.string.volume),
            valueFormatter = { value, _ ->
                var num =
                    value.toDouble().toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
                num = num.dropLastWhile { it == '0' }
                if (num.last() == '.') num.dropLast(1) else num
            },
            itemPlacer = AxisItemPlacer.Vertical.default(
                maxItemCount = 8
            ),
        ),
        bottomAxis = rememberBottomAxis(
            title = stringResource(R.string.workout),
            valueFormatter = { value, _ ->
                "${value.toInt() + 1}."
            },
            guideline = null,
        ),
        marker = marker,
        modifier = Modifier.height(workoutTrackerDimens.chartHeight)
    )
}
