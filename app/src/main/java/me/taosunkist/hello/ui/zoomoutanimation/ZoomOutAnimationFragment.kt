package me.taosunkist.hello.ui.zoomoutanimation

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentZoomOutAnimationBinding

const val propertyNameInScaleX = "scaleX"

const val propertyNameInScaleY = "scaleY"

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ZoomOutAnimationFragment : Fragment() {

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    private val hideHandler = Handler()

    @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private var dummyButton: Button? = null

    private var fullscreenContentControls: View? = null

    private lateinit var binding: FragmentZoomOutAnimationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentZoomOutAnimationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dummyButton.setOnClickListener { dummyButtonPressed() }
    }

    private fun dummyButtonPressed() {

        Toasty.normal(requireContext(), "dummyButtonPressed")?.show()
        binding.fullscreenContentTextView.setImageResource(R.drawable.bg_blur_fake)
        val overshootCenterBalloonLibraryAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_show_up_center_balloon_library)
        /*AnimationUtils.loadAnimation(requireContext(), R.anim.anim_elastic_center_balloon_library)*/
        /*AnimationUtils.loadAnimation(requireContext(), R.anim.anim_overshoot_center_balloon_library)*/
        binding.fullscreenContentTextView.startAnimation(overshootCenterBalloonLibraryAnim)

        /*val screenHeightRadius = screenHeight / 2
        val screenWidthRadius = screenWidth / 2
        val animator = ViewAnimationUtils.createCircularReveal(binding.fullscreenContentTextView,
            screenWidthRadius,
            screenHeightRadius,
            0f,
            screenHeightRadius.toFloat())
        animator.duration = 800
        animator.interpolator = AccelerateInterpolator()
        animator.start()*/
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        dummyButton = null
        fullscreenContentControls = null
    }
}