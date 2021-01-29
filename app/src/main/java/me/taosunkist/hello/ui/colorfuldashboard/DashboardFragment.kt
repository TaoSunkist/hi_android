package me.taosunkist.hello.ui.colorfuldashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.NavHostFragment
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentDashboardBinding

class DashboardFragment : NavHostFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment().apply {}
    }

    lateinit var binding: FragmentDashboardBinding

    private val appCompatSeekBarChangeListener: SeekBar.OnSeekBarChangeListener by lazy {
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.arcProgressBar.setProgress(progress)
                binding.creditScoresDashboard.setProgressValue(progress.toFloat(), seekBar.max.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
           
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentDashboardBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arcProgressBar.setMaxProgress(binding.appCompatSeekBar.max)
        binding.creditScoresDashboard.setMaxProgress(binding.appCompatSeekBar.max)
        binding.appCompatSeekBar.setOnSeekBarChangeListener(appCompatSeekBarChangeListener)
    }
}