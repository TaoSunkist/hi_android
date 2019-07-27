package me.taosunkist.hello.ui.colorfuldashboard

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import me.taosunkist.hello.R

import me.taosunkist.hello.ui.colorfuldashboard.view.dashboard.ArcProgressBar
import me.taosunkist.hello.ui.colorfuldashboard.view.dashboard.CreditScoresDashboard

class DashboardActivity : AppCompatActivity() {

    var mSeekBar: AppCompatSeekBar? = null
    var mArcProgressBar: ArcProgressBar? = null
    var mDashboard: CreditScoresDashboard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        init()
    }

    fun init() {
        mSeekBar = findViewById(R.id.app_compat_seek_bar)
        mArcProgressBar = findViewById(R.id.arc_progress_bar)
        mDashboard = findViewById(R.id.credit_scores_dashboard)

        mArcProgressBar!!.setMaxProgress(mSeekBar!!.max)
        mDashboard!!.setMaxProgress(mSeekBar!!.max)
        mSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mArcProgressBar!!.setProgress(progress)
                mDashboard!!.setProgressValue(progress.toFloat(), seekBar?.max!!.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
