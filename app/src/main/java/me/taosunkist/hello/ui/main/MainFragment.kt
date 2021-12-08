package me.taosunkist.hello.ui.main

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
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
import me.taosunkist.hello.databinding.FragmentMainBinding
import top.thsunkist.appkit.utility.StringUtil
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

        binding.toolbar.setOnMenuItemClickListener(this)

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

        val nick = "taohui"
        val targetNick = "wangbo"
        val roomID = System.currentTimeMillis()
        val result = getString(R.string.s_s_send_to_, nick, roomID.toString(), targetNick)

        val spannableStringBuilder = SpannableStringBuilder(result)
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, nick.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val roomIDPositionPair = StringUtil.findKeyWordPositionInTextPart(result, roomID.toString())
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")),
            roomIDPositionPair.first, roomIDPositionPair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val targetNickPositionPair = StringUtil.findKeyWordPositionInTextPart(result, targetNick)
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")),
            targetNickPositionPair.first, targetNickPositionPair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.imageSpanTextView.text = spannableStringBuilder

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
            R.id.nav_test_recycler_view -> {
                val direction = MainFragmentDirections.action2NavMultiListFragment()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(direction)
            }
            R.id.nav_animation -> {
            }
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        ToastyExt.error(this, "" + item.itemId)
        return true
    }
}

