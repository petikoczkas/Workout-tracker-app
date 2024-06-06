package hu.bme.aut.workout_tracker.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import hu.bme.aut.workout_tracker.R

object StepCounterWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    fun WidgetContent() {
        val progressIndicatorColor = Color(0xFF0040FF)
        val prefs = currentState<Preferences>()
        val currentSteps = prefs[StepCounterWidgetReceiver.currentSteps]
        val stepCounterUiState = StepCounterUiState(
            steps = currentSteps ?: 0,
        )
        Column(
            modifier = GlanceModifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 8.dp)
                    .background(ImageProvider(R.drawable.backgorund_widget)),
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = GlanceModifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stepCounterUiState.steps.toString(),
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Goal: 10000 steps",
                            style = TextStyle(
                                color = ColorProvider(Color.Gray),
                                fontSize = 10.sp,
                            )
                        )
                    }
                    Image(
                        provider = ImageProvider(R.drawable.ic_footprint),
                        modifier = GlanceModifier.size(38.dp),
                        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                        contentDescription = null
                    )
                }
                LinearProgressIndicator(
                    progress = stepCounterUiState.steps / 10000f,
                    modifier = GlanceModifier.fillMaxWidth().height(5.dp)
                        .padding(horizontal = 8.dp),
                    color = ColorProvider(progressIndicatorColor),
                    backgroundColor = ColorProvider(progressIndicatorColor.copy(alpha = 0.3f))
                )
            }
            Text(
                text = "Workout Tracker",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 12.sp
                ),
                modifier = GlanceModifier.padding(top = 4.dp)
            )
        }
    }
}
