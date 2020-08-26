package me.taosunkist.hello.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewAnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.controller.home.HomeActivity
import me.taosunkist.hello.ui.frgment.colorfuldashboard.DashboardActivity
import me.taosunkist.hello.ui.frgment.countdowntimer.squareprogressbar.CountdownTimeFragment
import me.taosunkist.hello.ui.frgment.grpc.GrpcFragment
import me.taosunkist.hello.ui.frgment.notification.ReminderFragment
import me.taosunkist.hello.ui.frgment.watermark.WatermarkFragment
import me.taosunkist.hello.ui.frgment.progress.SquareProgressBarFragment
import me.taosunkist.hello.ui.reusable.views.GenderAgeTextView
import kotlin.math.max

/*Gradle execute generateDebugProto*/
class AppEntranceActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_app_entrance)
		init()
	}

	private fun init() {
//		val toolbar = findViewById<Toolbar>(R.id.toolbar)
//		setSupportActionBar(toolbar)

		val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
		val navigationView: NavigationView = findViewById(R.id.activity_main_nav_view)
//		val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//
//		drawer.addDrawerListener(toggle)
//		toggle.syncState()
		navigationView.setNavigationItemSelectedListener(this)
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
//		supportFragmentManager.beginTransaction().add(R.id.content_root, CountdownTimeFragment.newInstance()).addToBackStack(CountdownTimeFragment.TAG).commitAllowingStateLoss()
		val genderAgeTextView  = findViewById<GenderAgeTextView>(R.id.gender_age_text_view)
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
		val id = item.itemId

		when (id) {
			R.id.nav_camera -> {
			}
			R.id.nav_share -> {
				supportFragmentManager.beginTransaction().add(R.id.content_root, ReminderFragment.newInstance()).addToBackStack(ReminderFragment.TAG).commitAllowingStateLoss()
			}
			R.id.nav_tatame_battery -> {
				startActivity(Intent(this, HomeActivity::class.java))
			}
			R.id.nav_grpc -> {
				supportFragmentManager.beginTransaction().add(R.id.content_root, GrpcFragment.newInstance()).addToBackStack(GrpcFragment.TAG).commitAllowingStateLoss()
			}
			R.id.nav_progress -> {
				supportFragmentManager.beginTransaction().add(R.id.content_root, SquareProgressBarFragment.newInstance()).addToBackStack(SquareProgressBarFragment.TAG).commitAllowingStateLoss()
			}
		}

		val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
		drawer.closeDrawer(GravityCompat.START)
		return true
	}
}

