package me.taosunkist.hello.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ActivityAppEntranceBinding
import me.taosunkist.hello.ui.notification.ReminderFragment
import me.taosunkist.hello.ui.squareprogress.SquareProgressBarFragment
import me.taosunkist.hello.ui.watermark.WatermarkFragment

class AppEntranceActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityAppEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_entrance)
        setSupportActionBar(binding.toolbar)
        ActionBarDrawerToggle(this,
			binding.drawerLayout,
			binding.toolbar,
			R.string.navigation_drawer_open,
			R.string.navigation_drawer_close).apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }
        binding.activityMainNavigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.beginTransaction().add(binding.contentRoot.id, SquareProgressBarFragment.newInstance())
            .addToBackStack(SquareProgressBarFragment.TAG).commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            supportFragmentManager.beginTransaction()
                .add(binding.contentRoot.id, WatermarkFragment.newInstance())
                .addToBackStack(WatermarkFragment.tag).commitAllowingStateLoss()
            return true
        } else if (id == R.id.action_dashboard) {
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
			R.id.nav_main -> {
			}
			R.id.nav_share -> {
				supportFragmentManager.beginTransaction().add(binding.contentRoot.id, ReminderFragment.newInstance())
					.addToBackStack(ReminderFragment.TAG).commitAllowingStateLoss()
			}
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

