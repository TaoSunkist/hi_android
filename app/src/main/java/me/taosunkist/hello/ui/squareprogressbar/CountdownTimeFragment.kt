package me.taosunkist.hello.ui.squareprogressbar

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentCountdownTimeBinding
import me.taosunkist.hello.ui.squareprogressbar.CountdownTimeFragment.Companion.TAG
import me.taosunkist.hello.ui.progress.SquareProgressBar
import me.taosunkist.hello.utility.printf

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CountdownTimeFragment : Fragment() {

	companion object {
		const val TAG: String = "CountdownTime"

		fun newInstance() = CountdownTimeFragment()
	}

	private lateinit var binding: FragmentCountdownTimeBinding

	private var countDownTimer: ProgressCountDownTimer? = null


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View = FragmentCountdownTimeBinding.inflate(inflater, container, false).apply {
		binding = this
	}.root

	/*Unit(s)*/
	private var totalDuration: Double = 6.0

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		countDownTimer = ProgressCountDownTimer(CountDownTimerPreloadModel(
			total = totalDuration * 1000,
			currentCountDownValue = totalDuration.toLong() * 1000,
			millisInFuture = totalDuration.toLong() * 1000,
			countDownInterval = countDownInterval.toLong()
		), binding.squareProgressBar)

		binding.squareProgressBar.setProgress(50)
		binding.squareProgressBar.setColor(Color.GREEN)
		binding.squareProgressBar.setHoloColor(R.color.black)
		binding.squareProgressBar.isRoundedCorners = true
		binding.addButton.setOnClickListener { addButtonPressed() }
		binding.pauseButton.setOnClickListener { pauseButtonPressed() }
		binding.resumeButton.setOnClickListener { resumeButtonPressed() }
		binding.startButton.setOnClickListener { startButtonPressed() }

	}

	private var countDownInterval = 100.0

	private fun startButtonPressed() {
		countDownTimer?.start()
	}

	private fun resumeButtonPressed() {}

	private fun pauseButtonPressed() {}


	private fun addButtonPressed() {
		val currentCountDownValue = countDownTimer?.preloadModel?.currentCountDownValue
		countDownTimer?.preloadModel?.let {
			val preloadModel = CountDownTimerPreloadModel(
				total = it.currentCountDownValue + totalDuration * 1000,
				currentCountDownValue = it.currentCountDownValue + totalDuration.toInt() * 1000,
				millisInFuture = it.currentCountDownValue + totalDuration.toInt() * 1000,
				countDownInterval = countDownInterval.toLong()
			)
			binding.squareProgressBar.progress = 0.0
			currentCountDownValue?.let {
				countDownTimer?.cancel()
				countDownTimer = null
				countDownTimer = ProgressCountDownTimer(squareProgressBar = binding.squareProgressBar,
					preloadModel = preloadModel)
				countDownTimer?.start()
			}
		}
	}
}

data class CountDownTimerPreloadModel(
	var total: Double = 0.0,
	var currentCountDownValue: Long = 0,
	var millisInFuture: Long,
	val countDownInterval: Long
)

class ProgressCountDownTimer(val preloadModel: CountDownTimerPreloadModel, val squareProgressBar: SquareProgressBar) : CountDownTimer(preloadModel.millisInFuture, preloadModel.countDownInterval) {
	override fun onFinish() {
		squareProgressBar.progress = 0.0
	}

	override fun onTick(millisUntilFinished: Long) {
		preloadModel.currentCountDownValue = millisUntilFinished
		squareProgressBar.progress = millisUntilFinished / preloadModel.total * 100
		printf(TAG, millisUntilFinished, " ${millisUntilFinished / preloadModel.total * 100}")
	}
}