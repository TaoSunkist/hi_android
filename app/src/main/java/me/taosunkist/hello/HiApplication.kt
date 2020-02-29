package me.taosunkist.hello

import androidx.multidex.MultiDexApplication
import com.example.tatame_component.init

/**
 * Created by sunkist on 2019-07-27
 */
class HiApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        init()
    }
}