package com.example.core.result

/**
 * A sealed interface representing the result of an operation in Clean Architecture.
 * Provides functional operators like [fold], [onSuccess], [onError], [map], [getOrNull], and [getOrElse].
 */
sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val cause: Throwable, val message: String = cause.localizedMessage ?: "Unknown error") : AppResult<Nothing>

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    companion object {
        inline fun <T> runCatching(block: () -> T): AppResult<T> = try {
            Success(block())
        } catch (e: Throwable) {
            Error(cause = e, message = e.message ?: "An unexpected error occurred")
        }
    }
}

fun <T> AppResult<T>.getOrNull(): T? = when (this) {
    is AppResult.Success -> data
    is AppResult.Error -> null
}

fun <T> AppResult<T>.getOrElse(defaultValue: T): T = when (this) {
    is AppResult.Success -> data
    is AppResult.Error -> defaultValue
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AppResult.Error) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(this)
    return this
}

inline fun <T, R> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (AppResult.Error) -> R
): R = when (this) {
    is AppResult.Success -> onSuccess(data)
    is AppResult.Error -> onError(this)
}
