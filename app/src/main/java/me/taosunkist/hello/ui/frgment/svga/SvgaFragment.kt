package me.taosunkist.hello.ui.frgment.svga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.taosunkist.hello.R
class SvgaFragment : Fragment() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_svga, container, false)
	}

	companion object {
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
			SvgaFragment().apply {
				arguments = Bundle().apply {
				}
			}
	}
}