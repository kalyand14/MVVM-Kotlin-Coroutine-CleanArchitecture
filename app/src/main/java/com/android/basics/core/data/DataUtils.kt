package com.android.basics.core.data

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

public suspend fun <T, R> query(
    ioDispatcher: CoroutineDispatcher,
    statement: () -> T,
    transform: (T) -> R,
    predicate: (T?) -> Boolean
): Either<Failure, R> = withContext(ioDispatcher) {
    try {
        val response: T = statement()
        if (predicate(response)) {
            return@withContext Either.Right(transform(response))
        } else {
            return@withContext Either.Left(Failure.DataError(Constants.NOT_FOUND))
        }
    } catch (e: Exception) {
        return@withContext Either.Left(Failure.Error(e))
    }
}