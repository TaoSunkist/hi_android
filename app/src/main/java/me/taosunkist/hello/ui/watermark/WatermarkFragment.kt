package me.taosunkist.hello.ui.watermark

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.taosunkist.hello.R

class WatermarkFragment : Fragment() {

    companion object {
        fun newInstance() = WatermarkFragment()
    }

    private lateinit var viewModel: WatermarkViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.watermark_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WatermarkViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
