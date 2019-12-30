package com.racovita.wow.utils.extensions

import android.view.View
import com.jakewharton.rxbinding.view.RxView
import java.util.concurrent.TimeUnit

fun View.show() = apply { visibility = View.VISIBLE }

fun View.hide() = apply { visibility = View.GONE }

fun View.debouncedClick(invoke: () -> Unit) {
    RxView.clicks(this)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe { invoke() }
}