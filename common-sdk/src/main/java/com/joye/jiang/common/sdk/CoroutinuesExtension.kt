package com.joye.jiang.common.sdk

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun launchWithExpHandler(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = GlobalScope.launch(context + ExceptionHandler, start, block)


fun <T> asyncWithExpHandler(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
) = GlobalScope.async(context + ExceptionHandler, start, block)

val ExceptionHandler by lazy {
    CoroutineExceptionHandler { _, throwable ->
    }
}

val mainHandler by lazy {
    Handler(Looper.getMainLooper())
}

inline fun runOnUi(noinline block: () -> Unit) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        block()
    } else {
        mainHandler.post(block)
    }
}

inline fun LifecycleOwner.launchAndRepeatWithViewLifecycle(
    dispatchers: CoroutineDispatcher = Dispatchers.IO,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    this.lifecycleScope.launch(dispatchers+ ExceptionHandler) {
        this@launchAndRepeatWithViewLifecycle.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}