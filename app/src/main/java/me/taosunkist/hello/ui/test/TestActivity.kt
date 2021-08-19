package me.taosunkist.hello.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.taosunkist.hello.R
import me.taosunkist.hello.utility.printf

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        printf("taohui","TestActivity onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        printf("taohui","TestActivity onDestroy")
    }
}