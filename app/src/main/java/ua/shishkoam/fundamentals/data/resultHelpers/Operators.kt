@file:Suppress("unused")

package by.kirich1409.result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> RequestResult<T>.isSuccess(): Boolean {
    return this is RequestResult.Success
}

fun <T> RequestResult<T>.asSuccess(): RequestResult.Success<T> {
    return this as RequestResult.Success<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> RequestResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies(this@isFailure is RequestResult.Failure<*>)
    }
    return this is RequestResult.Failure<*>
}

fun <T> RequestResult<T>.asFailure(): RequestResult.Failure<*> {
    return this as RequestResult.Failure<*>
}
