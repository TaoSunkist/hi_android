package me.taosunkist.hello.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.colorfuldashboard.DashboardActivity
import me.taosunkist.hello.ui.list.RecyclerViewOrientationFragment
import me.taosunkist.hello.ui.notification.NotificationFragment
import me.taosunkist.hello.ui.watermark.WatermarkFragment
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

//        val idolMoodView: IdolMoodView = findViewById(R.id.activity_main_idol_mood_view)
        val textView: AppCompatTextView = findViewById(R.id.content_main_text_view)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

//        findViewById<View>(R.id.go_grpc).setOnClickListener {
//            supportFragmentManager.beginTransaction().add(R.id.content_root, GrpcFragment.newInstance()).addToBackStack(GrpcFragment.tag).commitAllowingStateLoss()
//        }

        val text = textView.text

        val spannableString = SpannableString(text)
        val colorSpan = ForegroundColorSpan(Color.BLUE)
        spannableString.setSpan(colorSpan, text.indexOf(" "), spannableString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        val colorSpan2 = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(colorSpan2, 0, text.indexOf(" "), Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        textView.text = spannableString

        navigationView.setNavigationItemSelectedListener(this)
//        idolMoodView.also { it.switchIdolMood() }.setOnClickListener { idolMoodView.switchIdolMood() }

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

    override fun onResume() {
        super.onResume()
//        supportFragmentManager.beginTransaction().add(R.id.content_root, HomeFragment.newInstance()).addToBackStack(HomeFragment.TAG).commit()
//        supportFragmentManager.beginTransaction().add(R.id.content_root, ChatFragment()).addToBackStack(ChatFragment.TAG).commit()

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

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "nav_gallery", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "nav_slideshow", Toast.LENGTH_SHORT).show()
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

