package com.kgbier.kgbmd.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

@JvmInline
value class LiveDataDisposable(private val removeObserverClosure: () -> Unit) {
    fun dispose() = removeObserverClosure.invoke()
}

fun LiveDataDisposable.disposeBy(disposeBag: LiveDataDisposeBag) = disposeBag.add(this)

fun <T> LiveData<T>.disposable(observer: Observer<T>) =
    LiveDataDisposable { removeObserver(observer) }

class LiveDataDisposeBag {
    private var isDisposed = false
    private val disposables = mutableListOf<LiveDataDisposable>()

    fun add(liveDataDisposable: LiveDataDisposable) {
        if (isDisposed) {
            liveDataDisposable.dispose()
        } else {
            disposables.add(liveDataDisposable)
        }
    }

    fun dispose() {
        if (!isDisposed) {
            isDisposed = true
            disposables.forEach { it.dispose() }
            disposables.clear()
        }
    }
}

inline fun <T> LiveData<T>.bind(
    lifecycleOwner: LifecycleOwner,
    crossinline observerClosure: (T) -> Unit
): LiveDataDisposable {
    val observer = Observer<T> { observerClosure.invoke(it) }
    observe(lifecycleOwner, observer)
    return disposable(observer)
}
