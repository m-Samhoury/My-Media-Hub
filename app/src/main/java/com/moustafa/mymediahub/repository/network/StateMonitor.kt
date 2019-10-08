package com.moustafa.mymediahub.repository.network

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */


sealed class StateMonitor<out T> {
    object Loading : StateMonitor<Nothing>()
    object Init : StateMonitor<Nothing>()
    data class Loaded<T>(val result: T) : StateMonitor<T>()
    data class Failed(val failed: Throwable, val action: (() -> Any)? = null) :
        StateMonitor<Nothing>()
}
