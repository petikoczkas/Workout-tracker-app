package hu.bme.aut.workout_tracker.ui.widget

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StepCounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StepCounterWidget

    companion object {
        val currentSteps = intPreferencesKey("currentSteps")
    }

    private val coroutineScope = MainScope()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "hu.bme.aut.workout_tracker.STEP_COUNT_UPDATE") {
            val stepCount = intent.getIntExtra("stepCount", 0)
            coroutineScope.launch {
                val glanceId =
                    GlanceAppWidgetManager(context).getGlanceIds(StepCounterWidget::class.java)
                        .firstOrNull()
                glanceId?.let {
                    updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { pref ->
                        pref.toMutablePreferences().apply {
                            this[currentSteps] = stepCount
                        }
                    }
                    glanceAppWidget.update(context, it)
                }
            }
        }
    }
}