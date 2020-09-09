package me.taosunkist.hello.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewAnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ActivityAppEntranceBinding
import me.taosunkist.hello.ui.controller.home.HomeActivity
import me.taosunkist.hello.ui.frgment.colorfuldashboard.DashboardActivity
import me.taosunkist.hello.ui.frgment.grpc.GrpcFragment
import me.taosunkist.hello.ui.frgment.notification.ReminderFragment
import me.taosunkist.hello.ui.frgment.watermark.WatermarkFragment
import me.taosunkist.hello.ui.frgment.progress.SquareProgressBarFragment
import kotlin.math.max

class AppEntranceActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityAppEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_entrance)

        setSupportActionBar(binding.toolbar)
        ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }
        binding.activityMainNavView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            supportFragmentManager.beginTransaction().add(R.id.content_root, WatermarkFragment.newInstance()).addToBackStack(WatermarkFragment.tag).commitAllowingStateLoss()
            return true
        } else if (id == R.id.action_dashboard) {
            startActivity(Intent(this, DashboardActivity::class.java))
            val actionView = item.actionView
            if (actionView != null) {
                val actionViewCenterX = (actionView.left + actionView.right) / 2
                val actionHeightCenterY = (actionView.top + actionView.bottom) / 2
                val radius = max(actionView.width, actionView.height)
                var animator: android.animation.Animator? = null
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    animator = ViewAnimationUtils.createCircularReveal(actionView, actionViewCenterX, actionHeightCenterY, 0f, radius.toFloat())
                }
                animator!!.start()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_camera -> {
            }
            R.id.nav_share -> {
                supportFragmentManager.beginTransaction().add(binding.contentRoot.id, ReminderFragment.newInstance()).addToBackStack(ReminderFragment.TAG).commitAllowingStateLoss()
            }
            R.id.nav_tatame_battery -> {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            R.id.nav_grpc -> {
                supportFragmentManager.beginTransaction().add(binding.contentRoot.id, GrpcFragment.newInstance()).addToBackStack(GrpcFragment.TAG).commitAllowingStateLoss()
            }
            R.id.nav_progress -> {
                supportFragmentManager.beginTransaction().add(binding.contentRoot.id, SquareProgressBarFragment.newInstance()).addToBackStack(SquareProgressBarFragment.TAG).commitAllowingStateLoss()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

