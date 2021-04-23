package me.taosunkist.hello.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.NavigatorProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ActivityMainBinding
import me.taosunkist.hello.ui.profile.ProfileFragment
import me.taosunkist.hello.utility.ToastyExt

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navMain,
            R.id.navDashboard,
            R.id.navRadarView,
        ).setOpenableLayout(binding.drawerLayout).build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.navigationView.getHeaderView(0).avatarImageButton.setOnClickListener {
            if (it.tag == null) {
                it.tag = ""
                binding.navigationView.getHeaderView(0).rotateAnimationView.setBackgroundResource(
                    R.drawable.ic_searching_matching_float
                )
            } else {
                it.tag = null
                binding.navigationView.getHeaderView(0).rotateAnimationView.setBackgroundResource(
                    R.drawable.ic_matching_matching_float
                )
            }
            navHostFragment.findNavController().navigate(R.id.navProfile)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val mainFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = mainFragment.navController
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_menu_item_first -> {
            }
            R.id.bottom_menu_item_second -> {

            }
        }
        return true
    }
}