package me.taosunkist.hello.ui.progress

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tencent.mmkv.MMKV
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentSquareProgressBarBinding
import me.taosunkist.hello.utility.printf
import top.thsunkist.tatame.ui.reusable.GuideTipeLineView
import top.thsunkist.tatame.utilities.Dimens.marginSmall
import java.util.*
import java.util.concurrent.TimeUnit

class SquareProgressBarFragment : Fragment() {
    lateinit var squareProgressBar: SquareProgressBar
    lateinit var progressSeekBar: SeekBar
    lateinit var widthSeekBar: SeekBar
    private val currentProcess = 0

    init {

    }

    lateinit var binding: FragmentSquareProgressBarBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSquareProgressBarBinding.inflate(inflater, container, false)


        squareProgressBar = binding.subi2
        progressSeekBar = binding.progressSeekBar
        widthSeekBar = binding.widthSeekBar

        val view = binding.root
        Log.i(TAG, requireContext().cacheDir.absolutePath)
        MMKV.initialize(requireContext().cacheDir.absolutePath)
        binding.getExternalCacheDirButton.setOnClickListener { v: View? ->
            Log.i(TAG, "" + MMKV.defaultMMKV().putString("taohui", "sillyb").commit())
            Log.i(TAG, "" + MMKV.defaultMMKV().getString("taohui", "sillyb"))
        }
        view.setBackgroundColor(Color.GRAY)
        val progressView = view
                .findViewById<View>(R.id.progressDisplay) as TextView
        progressView.text = "32%"
        squareProgressBar = view.findViewById(R.id.subi2)
        squareProgressBar.setImage(R.drawable.blenheim_palace)
        squareProgressBar.setColor("#C9C9C9")
        squareProgressBar.setHoloColor(R.color.errorTextColor)
        squareProgressBar.setRoundedCorners(true, marginSmall.toFloat())
        squareProgressBar.setProgress(32)
        squareProgressBar.width = 8
        squareProgressBar.setOnClickListener(View.OnClickListener { view1: View? ->
            val random = Random()

            // random progress
            setProgressBarProgress(random.nextInt(100), progressView)

            // random width
            val randWidth = random.nextInt(17) + 4
            widthSeekBar!!.progress = randWidth
            squareProgressBar.setWidth(randWidth)

            // random colour
            squareProgressBar.setColorRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        })
        progressSeekBar = view
                .findViewById<View>(R.id.progressSeekBar) as SeekBar
        progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // nothing to do
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // nothing to do
            }

            override fun onProgressChanged(seekBar: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                setProgressBarProgress(progress, progressView)
            }
        })
        widthSeekBar = view.findViewById<View>(R.id.widthSeekBar) as SeekBar
        widthSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // nothing to do
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // nothing to do
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                squareProgressBar.setWidth(progress)
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lineView.setData(
                Arrays.asList("02-1", "02-1", "02-1", "02-1", "02-1", "02-1", "02-1"),
                Arrays.asList(1.0f, 1.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f))
        binding.guideTipLineView.setOnClickListener {
            binding.guideTipLineView.showGuideType(GuideTipeLineView.GuideType.values()[(0..2).random()])
        }
    }

    private fun setProgressBarProgress(progress: Int, progressView: TextView) {
        squareProgressBar.setProgress(progress)
        progressView.text = "$progress%"
        progressSeekBar.progress = progress
    }

    companion object {
        fun newInstance(): SquareProgressBarFragment {
            val args = Bundle()
            val fragment = SquareProgressBarFragment()
            fragment.arguments = args
            return fragment
        }

        const val TAG = "SquareFragment"
    }
}