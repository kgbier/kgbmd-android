package com.kgbier.kgbmd.dev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.kgbier.kgbmd.databinding.ComponentsActivityBinding
import com.kgbier.kgbmd.databinding.SettingsActivityBinding
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.PosterView

class DeveloperMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Developer Menu"

        binding.buttonComponents.setOnClickListener {
            startActivity(ComponentsActivity.makeIntent(this))
        }
    }
}

class ComponentsActivity : AppCompatActivity() {
    companion object {
        fun makeIntent(context: Context) = Intent(context, ComponentsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setHomeButtonEnabled(true)
        val binding = ComponentsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Components"

        PosterView(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                64.dp, 92.dp
            )
        }.also(binding.layout::addView)
    }
}
