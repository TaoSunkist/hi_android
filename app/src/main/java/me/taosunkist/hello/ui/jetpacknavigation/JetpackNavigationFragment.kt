package me.taosunkist.hello.ui.jetpacknavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.taosunkist.hello.R

class JetpackNavigationFragment : Fragment() {

	companion object {
		fun newInstance() = JetpackNavigationFragment()
	}

	private lateinit var viewModel: JetpackNavigationModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.jetpack_navigation_fragment, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this).get(JetpackNavigationModel::class.java)
	}
}