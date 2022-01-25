package me.taosunkist.hello.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.view.*
import me.taosunkist.hello.R
import me.taosunkist.hello.data.UserStore
import me.taosunkist.hello.databinding.FragmentMainBinding
import me.taosunkist.hello.databinding.NavHeaderMainBinding
import me.taosunkist.hello.ui.mutualheartbeat.MutualHeartbeatDialog
import me.taosunkist.hello.utility.ToastyExt

class MainFragment : NavHostFragment(), AppBarConfiguration.OnNavigateUpListener, NavigationView.OnNavigationItemSelectedListener,
    Toolbar.OnMenuItemClickListener {

    private lateinit var binding: FragmentMainBinding

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentMainBinding.inflate(inflater, container, false).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.dampingHorizonMovableViewButton.setOnClickListener {
        }
        binding.toolbar.setOnMenuItemClickListener(this)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navMain,
            R.id.navDashboard,
        ).setFallbackOnNavigateUpListener(
            this
        ).setOpenableLayout(binding.drawerLayout).build()

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

        UserStore.shared.login()

        binding.user = UserStore.shared.user
        binding.navigationView.setNavigationItemSelectedListener(this)

        val navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navigationView.getHeaderView(0))

        navHeaderMainBinding.user = binding.user
        navHeaderMainBinding.avatarImageButton.setOnClickListener { avatarImageButtonPressed(it) }

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

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(MainFragmentDirections.action2navProfile())
    }

    override fun onNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_radar_view -> {
                val direction = MainFragmentDirections.action2NavRadarUI()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(direction)
            }
            R.id.nav_text_span -> {
                val direction = MainFragmentDirections.action2NavTextSpanFragment()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(direction)
            }
            R.id.nav_recycler_view -> {
                val direction = MainFragmentDirections.action2NavMultiListFragment()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(direction)
            }
            R.id.nav_compose -> {
            }
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        ToastyExt.error(this, "" + item.itemId)
        when (item.itemId) {
            R.id.action_qr_code -> {
                MutualHeartbeatDialog.init(requireContext()).show()
            }
        }
        return true
    }
}

