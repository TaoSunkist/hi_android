package me.taosunkist.hello.ui.radarview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import me.taosunkist.hello.HiApplication
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentRadarViewBinding
import top.thsunkist.library.utilities.Dimens

private const val ARG_PARAM1 = "param1"

private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RadarViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RadarViewFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RadarViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private lateinit var binding: FragmentRadarViewBinding

    private var param1: String? = null

    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentRadarViewBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*(context?.applicationContext as HiApplication).refWatcher.watch(binding.rippleCircleDiffuseView)*/
    }

    override fun onResume() {
        super.onResume()
        view?.setOnClickListener { binding.rippleCircleDiffuseView.setFillWaveSourceShapeRadius(Dimens.dpToPx(15).toFloat()) }
        /*binding.radarView.startLoadingAnimation()*/
    }

    override fun onPause() {
        super.onPause()
        /*binding.rippleCircleDiffuseView.stop()*/
        /*binding.radarView.stopAnimation()*/
    }
}