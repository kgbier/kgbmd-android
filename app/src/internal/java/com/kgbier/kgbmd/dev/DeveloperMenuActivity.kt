package com.kgbier.kgbmd.dev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.kgbier.kgbmd.R
import com.kgbier.kgbmd.util.dp
import com.kgbier.kgbmd.view.ui.PosterView
import kotlinx.android.synthetic.internal.components_activity.*
import kotlinx.android.synthetic.internal.settings_activity.*

class DeveloperMenuActivity : AppCompatActivity(R.layout.settings_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Developer Menu"

        buttonComponents.setOnClickListener {
            startActivity(ComponentsActivity.makeIntent(this))
        }
    }
}

class ComponentsActivity : AppCompatActivity(R.layout.components_activity) {
    companion object {
        fun makeIntent(context: Context) = Intent(context, ComponentsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeButtonEnabled(true)

        title = "Components"

        PosterView(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                64.dp(), 92.dp()
            )
        }.also(layout::addView)
    }
}
