package com.example.tatame_frontline.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.example.tatame_frontline.R
import com.example.tatame_frontline.ui.home.adapters.HomeViewPagerAdapter

class HomeFragment : Fragment() {

    companion object {
        const val TAG = "tagHome"
        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply { } }
    }

    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = view.findViewById(R.id.view_pager2)
        val adapter = HomeViewPagerAdapter()
        viewPager2.adapter = adapter
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                println("taohui HomeFragment, onPageScrollStateChanged ${state}")
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                println("taohui HomeFragment, onPageScrolled ${position}, ${positionOffset}, ${positionOffsetPixels}")
            }

            override fun onPageSelected(position: Int) {
                println("taohui HomeFragment, onPageSelected ${position}")
            }
        })
    }
}
