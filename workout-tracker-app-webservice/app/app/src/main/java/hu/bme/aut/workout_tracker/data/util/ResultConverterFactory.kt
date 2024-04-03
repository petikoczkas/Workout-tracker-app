package hu.bme.aut.workout_tracker.data.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import hu.bme.aut.workout_tracker.data.model.auth.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultConverterFactory private constructor(private val moshi: Moshi) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (getRawType(type) != Result::class.java) return null

        val resultType = type as ParameterizedType
        val resultActualType = resultType.actualTypeArguments.firstOrNull()
            ?: throw IllegalStateException("Result must have a generic type")

        return when (resultActualType) {
            AuthResponse::class.java -> {
                val authResponseAdapter = moshi.adapter(AuthResponse::class.java)
                AuthResponseResultConverter(authResponseAdapter)
            }

            Unit::class.java -> UnitResultConverter()
            else -> throw IllegalStateException("Unsupported Result type")
        }
    }

    private inner class AuthResponseResultConverter(
        private val authResponseAdapter: JsonAdapter<AuthResponse>
    ) : Converter<ResponseBody, Result<AuthResponse>> {

        override fun convert(value: ResponseBody): Result<AuthResponse>? {
            return try {
                val json = value.use { it.string() }
                val resultJson = authResponseAdapter.fromJson(json) ?: return null
                Result.success(resultJson)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private inner class UnitResultConverter : Converter<ResponseBody, Result<Unit>> {
        override fun convert(value: ResponseBody): Result<Unit>? {
            return try {
                value.use {
                    Result.success(Unit)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    companion object {
        fun create(moshi: Moshi): ResultConverterFactory {
            return ResultConverterFactory(moshi)
        }
    }
}
