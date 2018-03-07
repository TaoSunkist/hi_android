package me.taosunkist.hello

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSeekBar
import android.widget.SeekBar
import me.taosunkist.uilib.dashboard.ArcProgressBar
import me.taosunkist.uilib.dashboard.CreditScoresDashboard

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
        mSeekBar = findViewById(R.id.app_compat_seek_bar) as AppCompatSeekBar?
        mArcProgressBar = findViewById(R.id.arc_progress_bar) as ArcProgressBar
        mDashboard = findViewById(R.id.credit_scores_dashboard) as CreditScoresDashboard

        mArcProgressBar!!.setMaxProgress(mSeekBar!!.max)
        mDashboard!!.setMaxProgress(mSeekBar!!.max)
        mSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mArcProgressBar!!.setProgress(progress)
                mDashboard!!.setProgressValue(progress.toFloat(), seekBar?.max!!.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}
