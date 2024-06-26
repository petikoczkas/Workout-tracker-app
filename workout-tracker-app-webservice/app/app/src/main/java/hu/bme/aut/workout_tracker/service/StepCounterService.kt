package hu.bme.aut.workout_tracker.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.widget.StepCounterWidgetReceiver
import java.util.Calendar

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private val stepCounterWidgetReceiver = StepCounterWidgetReceiver()
    private var stepSensor: Sensor? = null
    private var currentStepCount = 0
    private var initialStepCount = 0

    companion object {
        private const val PREFS_NAME = "StepCounterPrefs"
        private const val KEY_INITIAL_STEP_COUNT = "initialStepCount"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        registerReceiver(
            stepCounterWidgetReceiver,
            IntentFilter("hu.bme.aut.workout_tracker.STEP_COUNT_UPDATE")
        )
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        loadInitialStepCount()
        startForegroundService()
        scheduleMidnightReset()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        unregisterReceiver(stepCounterWidgetReceiver)
        sensorManager.unregisterListener(this)
        cancelMidnightReset()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount == 0) {
                initialStepCount = event.values[0].toInt()
                saveInitialStepCount()
            }
            currentStepCount = event.values[0].toInt() - initialStepCount
            val intent = Intent("hu.bme.aut.workout_tracker.STEP_COUNT_UPDATE")
            intent.putExtra("stepCount", currentStepCount)
            sendBroadcast(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("step_counter_service", "Step Counter Service")
            } else {
                ""
            }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Step Counter Service")
            .setContentText("Service is running...")
            .setSmallIcon(R.drawable.ic_workout)
            .build()

        startForeground(1, notification)

        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    private fun scheduleMidnightReset() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ResetReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelMidnightReset() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ResetReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    private fun saveInitialStepCount() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(KEY_INITIAL_STEP_COUNT, initialStepCount)
            apply()
        }
    }

    private fun loadInitialStepCount() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        initialStepCount = sharedPreferences.getInt(KEY_INITIAL_STEP_COUNT, 0)
    }

    class ResetReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null) {
                val sharedPreferences =
                    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putInt(KEY_INITIAL_STEP_COUNT, 0)
                    apply()
                }
                val serviceIntent = Intent(context, StepCounterService::class.java)
                context.stopService(serviceIntent)
                context.startService(serviceIntent)
            }
        }
    }
}
