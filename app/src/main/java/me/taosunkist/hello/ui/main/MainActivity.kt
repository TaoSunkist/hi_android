package me.taosunkist.hello.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ActivityMainBinding
import me.taosunkist.hello.ui.reusable.DebugFloatView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val debugFloatView = DebugFloatView(this)
        debugFloatView.show()
        debugFloatView.setOnClickListener { v -> debugFloatViewPressed() }
    }

    private fun debugFloatViewPressed() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}