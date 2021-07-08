package me.taosunkist.hello.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.view.*
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentMainBinding
import me.taosunkist.hello.utility.ToastyExt


class MainFragment : NavHostFragment(), AppBarConfiguration.OnNavigateUpListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentMainBinding

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentMainBinding.inflate(inflater, container, false).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        /*appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navMain,
            R.id.navDashboard,
            R.id.navRadarView,
        ).setFallbackOnNavigateUpListener(
            this
        ).setOpenableLayout(binding.drawerLayout).build()*/

        binding.drawerLayout.addDrawerListener(
            ActionBarDrawerToggle(requireActivity(),
                binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ).apply {
                syncState()
            }
        )

        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.navigationView.getHeaderView(0).avatarImageButton.setOnClickListener { avatarImageButtonPressed(it) }

        binding.recyclerView.adapter = MainListAdapter()
    }

    private fun avatarImageButtonPressed(it: View) {
        if (it.tag == null) {
            it.tag = ""
            binding.navigationView.getHeaderView(0).rotateAnimationView.setBackgroundResource(R.drawable.ic_searching_matching_float)
        } else {
            it.tag = null
            binding.navigationView.getHeaderView(0).rotateAnimationView.setBackgroundResource(R.drawable.ic_matching_matching_float)
        }
    }

    override fun onNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_radar_view -> {
                val direction = MainFragmentDirections.actionInMainDrawerLayoutMenuRadarItemPressed()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(direction)
            }
        }
        return true
    }
}

