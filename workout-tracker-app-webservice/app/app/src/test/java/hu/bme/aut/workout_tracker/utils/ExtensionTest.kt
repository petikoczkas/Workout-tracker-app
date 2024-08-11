package hu.bme.aut.workout_tracker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

class ExtensionsTest {

    @Test
    fun `test valid email`() {
        assertThat("test@example.com".isValidEmail()).isTrue()
        assertThat("invalid-email".isValidEmail()).isFalse()
        assertThat("test@.com".isValidEmail()).isFalse()
        assertThat("".isValidEmail()).isFalse()
    }

    @Test
    fun `test valid password`() {
        assertThat("Password1".isValidPassword()).isTrue()
        assertThat("password".isValidPassword()).isFalse()
        assertThat("PASSWORD".isValidPassword()).isFalse()
        assertThat("Passw1".isValidPassword()).isFalse()
        assertThat("".isValidPassword()).isFalse()
    }

    @Test
    fun `test password matches`() {
        assertThat("password".passwordMatches("password")).isTrue()
        assertThat("password".passwordMatches("different")).isFalse()
    }

    @Test
    fun `test remove empty lines`() {
        assertThat("  text  ".removeEmptyLines()).isEqualTo("text")
        assertThat("\n\ntext\n\n".removeEmptyLines()).isEqualTo("text")
        assertThat("".removeEmptyLines()).isEqualTo("")
        assertThat(" text with spaces ".removeEmptyLines()).isEqualTo("text with spaces")
    }

    @Test
    fun `test capitalize words`() {
        assertThat("hello world".capitalizeWords()).isEqualTo("Hello World")
        assertThat("hello (world)".capitalizeWords()).isEqualTo("Hello (World)")
        assertThat("hello".capitalizeWords()).isEqualTo("Hello")
    }

    @Test
    fun `test get byte array`() {
        mockkStatic(BitmapFactory::class)
        mockkStatic(Bitmap::class)
        mockkConstructor(Matrix::class)

        val mockBitmap: Bitmap = mockk(relaxed = true)
        val inputStream: InputStream = ByteArrayInputStream(ByteArray(1024))

        every { BitmapFactory.decodeStream(any<InputStream>()) } returns mockBitmap
        every { Bitmap.createScaledBitmap(any(), any(), any(), any()) } returns mockBitmap
        every { Bitmap.createBitmap(mockBitmap, any(), any(), any(), any(), any(), any()) } returns mockBitmap
        every { mockBitmap.width } returns 100
        every { mockBitmap.height } returns 100
        every { mockBitmap.compress(any(), any(), any()) } returns true
        every { anyConstructed<Matrix>().postRotate(any()) } returns true

        val byteArray = inputStream.getByteArray(100, 100, 80)

        assertThat(byteArray).isNotNull()
    }
}
