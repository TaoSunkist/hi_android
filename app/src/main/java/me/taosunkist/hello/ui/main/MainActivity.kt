package me.taosunkist.hello.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ActivityMainBinding
import me.taosunkist.hello.ui.reusable.DebugFloatView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


    }

    override fun onDestroy() {
        super.onDestroy()
    }
}