package com.kgbier.kgbmd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.kgbier.kgbmd.view.MainLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

const val TAG = "APP"

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), LifecycleOwner {
    private lateinit var mainLayout: MainLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = MainLayout(this)
        setContentView(mainLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}