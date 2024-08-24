package hu.bme.aut.workout_tracker.data.api

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.SavedExercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.data.model.auth.AuthResponse
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthLogin
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthRegister
import hu.bme.aut.workout_tracker.data.util.ByteArrayAdapter
import hu.bme.aut.workout_tracker.data.util.ResultConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@ExperimentalCoroutinesApi
class WorkoutTrackerAPITest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: WorkoutTrackerAPI

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(ByteArrayAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(ResultConverterFactory.create(moshi))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(WorkoutTrackerAPI::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test isAvailable`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val result = api.isAvailable()
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test registrate`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val userAuthRegister = UserAuthRegister("email@example.com", "password", "test", "user")
        val result = api.registrate(userAuthRegister)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test signIn`() = runBlocking {
        val authResponse = AuthResponse("token")
        val jsonResponse = "{\"token\":\"token\"}"
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val userAuthLogin = UserAuthLogin("email@example.com", "password")
        val result = api.signIn(userAuthLogin)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.token).isEqualTo(authResponse.token)
    }

    @Test
    fun `test getCurrentUser`() = runBlocking {
        val user = User("email@example.com", "test", "user", byteArrayOf())
        val jsonResponse = """
            {
                "email": "email@example.com",
                "firstName": "test",
                "lastName": "user",
                "photo": ""
            }
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getCurrentUser("Bearer token", "email@example.com")
        assertThat(result.email).isEqualTo(user.email)
    }

    @Test
    fun `test getUsers`() = runBlocking {
        val users = listOf(User("email@example.com", "test", "user", byteArrayOf()))
        val jsonResponse = """
            [
                {
                    "email": "email@example.com",
                    "firstName": "test",
                    "lastName": "user",
                    "photo": ""
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getUsers("Bearer token")
        assertThat(result).isEqualTo(users)
    }

    @Test
    fun `test updateUser`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val user = User("email@example.com", "test", "user", byteArrayOf())
        val result = api.updateUser("Bearer token", user)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test getExercises`() = runBlocking {
        val exercises = listOf(Exercise(1, "Chest", "Push Up"))
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "category": "Chest",
                    "name": "Push Up"
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getExercises("Bearer token")
        assertThat(result).isEqualTo(exercises)
    }

    @Test
    fun `test getStandingsExercises`() = runBlocking {
        val exercises = listOf(Exercise(1, "Legs", "Squat"))
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "category": "Legs",
                    "name": "Squat"
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getStandingsExercises("Bearer token")
        assertThat(result).isEqualTo(exercises)
    }

    @Test
    fun `test createExercise`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val exercise = Exercise(1, "Chest", "Push Up")
        val result = api.createExercise("Bearer token", exercise)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test getUserWorkouts`() = runBlocking {
        val workouts = listOf(Workout(1, "1", "Morning Workout", false, mutableListOf()))
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "userId": "1",
                    "name": "Morning Workout",
                    "isFavorite": false,
                    "exercises": []
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getUserWorkouts("Bearer token", "email@example.com")
        assertThat(result).isEqualTo(workouts)
    }

    @Test
    fun `test getUserFavoriteWorkouts`() = runBlocking {
        val workouts = listOf(Workout(1, "1", "Morning Workout", true, mutableListOf()))
        val jsonResponse = "[{\"id\":1, \"userId\":\"1\", \"name\":\"Morning Workout\", \"isFavorite\":true, \"exercises\":[]}]"
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getUserFavoriteWorkouts("Bearer token", "email@example.com")
        assertThat(result).isEqualTo(workouts)
    }

    @Test
    fun `test getWorkout`() = runBlocking {
        val workout = Workout(1, "1", "Morning Workout", false, mutableListOf())
        val jsonResponse = """
            {
                "id": 1,
                "userId": "1",
                "name": "Morning Workout",
                "isFavorite": false,
                "exercises": []
            }
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getWorkout("Bearer token", 1)
        assertThat(result).isEqualTo(workout)
    }

    @Test
    fun `test updateWorkout`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val workout = Workout(1, "1", "Morning Workout", false, mutableListOf())
        val result = api.updateWorkout("Bearer token", workout)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test deleteWorkout`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val result = api.deleteWorkout("Bearer token", 1)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test getUserSavedExercises`() = runBlocking {
        val savedExercises =
            listOf(SavedExercise(1, "1", Exercise(1, "Chest", "Push Up"), mutableListOf()))
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "userId": "1",
                    "exercise": {
                        "id": 1,
                        "name": "Push Up",
                        "category": "Chest"
                    },
                    "data": []
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getUserSavedExercises("Bearer token", "email@example.com")
        assertThat(result).isEqualTo(savedExercises)
    }

    @Test
    fun `test updateSavedExercise`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val savedExercise = SavedExercise(1, "1", Exercise(1, "Chest", "Push Up"), mutableListOf())
        val result = api.updateSavedExercise("Bearer token", savedExercise)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `test getUserCharts`() = runBlocking {
        val charts = listOf(
            Chart(
                1,
                "1",
                Exercise(1, "Chest", "Push Up"),
                ChartType.Volume,
                mutableListOf()
            )
        )
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "userId": "1",
                    "exercise": {
                        "id": 1,
                        "name": "Push Up",
                        "category": "Chest"
                    },
                    "type": "Volume",
                    "data": []
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getUserCharts("Bearer token", "email@example.com")
        assertThat(result).isEqualTo(charts)
    }

    @Test
    fun `test getCharts`() = runBlocking {
        val charts = listOf(
            Chart(
                1,
                "1",
                Exercise(1, "Chest", "Push Up"),
                ChartType.Volume,
                mutableListOf()
            )
        )
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "userId": "1",
                    "exercise": {
                        "id": 1,
                        "name": "Push Up",
                        "category": "Chest"
                    },
                    "type": "Volume",
                    "data": []
                }
            ]
        """
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))
        val result = api.getCharts("Bearer token")
        assertThat(result).isEqualTo(charts)
    }

    @Test
    fun `test updateChart`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(200))
        val chart =
            Chart(1, "1", Exercise(1, "Chest", "Push Up"), ChartType.Volume, mutableListOf())
        val result = api.updateChart("Bearer token", chart)
        assertThat(result.isSuccess).isTrue()
    }
}
