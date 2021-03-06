package com.android.basics.core.interactor

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UseCaseTest {

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    private val useCase = MyUseCase()

    @Test
    fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }

        assertEquals(
            result, Either.Right(MyType(TYPE_TEST))
        )
    }

    @Test
    fun `should return correct data when executing use case`() {
        var result: Either<Failure, MyType>? = null

        val params = MyParams("TestParam")
        val onResult = { myResult: Either<Failure, MyType> -> result = myResult }

        runBlocking { useCase(params, onResult) }

        assertEquals(
            result, Either.Right(MyType(TYPE_TEST))
        )
    }


    data class MyType(val name: String)
    data class MyParams(val name: String)

    private inner class MyUseCase : UseCase<MyType, MyParams>() {
        override suspend fun run(params: MyParams) = Either.Right(MyType(TYPE_TEST))
    }
}