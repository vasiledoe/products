package com.racovita.wow.utils.extensions

import io.reactivex.disposables.Disposable

fun Disposable?.safelyDispose() = apply {
    if (this != null && !this.isDisposed) {
        this.dispose()
    }
}