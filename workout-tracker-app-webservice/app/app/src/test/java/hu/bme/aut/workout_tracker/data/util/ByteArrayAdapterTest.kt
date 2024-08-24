package hu.bme.aut.workout_tracker.data.util

import android.util.Base64
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test

class ByteArrayAdapterTest {

    private val adapter = ByteArrayAdapter()


    @Before
    fun setUp() {
        mockkStatic(Base64::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(Base64::class)
    }

    @Test
    fun `test fromJson with empty string`() {
        val result = adapter.fromJson("")
        assertThat(result).isEqualTo(ByteArray(0))
    }

    @Test
    fun `test fromJson with non-empty string`() {
        val originalByteArray = "Hello, World!".toByteArray()
        val base64String = java.util.Base64.getMimeEncoder().encodeToString(originalByteArray)

        every { Base64.decode(base64String, Base64.DEFAULT) } returns originalByteArray

        val result = adapter.fromJson(base64String)
        assertThat(result).isEqualTo(originalByteArray)
    }

    @Test
    fun `test toJson with empty ByteArray`() {
        val result = adapter.toJson(ByteArray(0))
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `test toJson with non-empty ByteArray`() {
        val byteArray = "Hello, World!".toByteArray()
        val expectedBase64String = java.util.Base64.getMimeEncoder().encodeToString(byteArray)

        every { Base64.encodeToString(byteArray, Base64.DEFAULT) } returns expectedBase64String

        val result = adapter.toJson(byteArray)
        assertThat(result).isEqualTo(expectedBase64String)
    }
}

