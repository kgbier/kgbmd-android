package com.kgbier.kgbmd.util

import androidx.activity.ComponentActivity

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelStore
import kotlin.reflect.KClass

/**
 * Adapted from [androidx.activity.viewModels]
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    key: String,
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return KeyedViewModelLazy(key, VM::class, { viewModelStore }, factoryPromise)
}

/**
 * Adapted from [androidx.lifecycle.ViewModelLazy]
 */
class KeyedViewModelLazy<VM : ViewModel>(
    private val key: String,
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> Factory
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory).get(key, viewModelClass.java).also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized() = cached != null
}
