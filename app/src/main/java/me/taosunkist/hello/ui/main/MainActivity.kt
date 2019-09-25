package me.taosunkist.hello.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewAnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.colorfuldashboard.DashboardActivity
import me.taosunkist.hello.ui.list.RecyclerViewOrientationFragment
import me.taosunkist.hello.ui.notification.NotificationFragment
//import me.taosunkist.hello.ui.grpc.GrpcFragment
import kotlin.math.max

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

//        findViewById<View>(R.id.go_grpc).setOnClickListener { supportFragmentManager.beginTransaction().add(R.id.content_root, GrpcFragment.newInstance()).addToBackStack(GrpcFragment.tag).commitAllowingStateLoss() }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
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

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            supportFragmentManager.beginTransaction().add(R.id.content_root, NotificationFragment.newInstance("", "")).addToBackStack(NotificationFragment.TAG).commitAllowingStateLoss()
        } else if (id == R.id.nav_send) {
            supportFragmentManager.beginTransaction().add(R.id.content_root, RecyclerViewOrientationFragment.newInstance()).addToBackStack(RecyclerViewOrientationFragment.tag).commitAllowingStateLoss()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
